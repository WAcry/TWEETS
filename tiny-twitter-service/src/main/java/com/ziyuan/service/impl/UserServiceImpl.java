package com.ziyuan.service.impl;

import com.ziyuan.enums.YesOrNo;
import com.ziyuan.mapper.UserMapper;
import com.ziyuan.pojo.User;
import com.ziyuan.pojo.UserExample;
import com.ziyuan.pojo.bo.UserBO;
import com.ziyuan.pojo.vo.UserVO;
import com.ziyuan.service.BaseService;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    private final static String DEFAULT_PROFILE_IMG_URL
            = "https://user-images.githubusercontent.com/61440144/184679060-f4dea2a7-98f5-42ba-a2fd-7efdbb39ceeb.png";
    @Autowired
    public UserMapper userMapper;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private KafkaOperator kafka;
    private SnowFlakeGenerator snowFlake;

    @Autowired
    public UserServiceImpl(SnowFlakeGenerator snowFlake) {
        this.snowFlake = snowFlake;
    }

    @Override
    public boolean auth(String userId, String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(token)) {
            return false;
        }
        String realToken = redis.get(USER_TOKEN + userId);
        if (!token.equals(realToken)) return false;

        long ttl = redis.ttl(USER_TOKEN + userId);
        if (ttl < USER_TOKEN_REFRESH_THRESHOLD) {
            String newToken = refreshToken(userId);
            redis.set(USER_TOKEN + userId, newToken);
            CookieUtils.setCookie(request, response, TOKEN_COOKIE, newToken);
        }

        return true;
    }

    @Override
    public UserVO getUserVOById(String userId) {
        User user = getUserById(userId);
        return convertUserToUserVO(user);
    }

    @Override
    public UserVO getUserVOByUsername(String username) {
        User user = getUserByUsername(username);
        return convertUserToUserVO(user);
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null) return null;

        String userStr = redis.get(USERNAME_INFO + username);

        if (StringUtils.isNotBlank(userStr)) {
            redis.expire(USERNAME_INFO + username, USERNAME_INFO_TTL);
            return JsonUtils.jsonToPojo(userStr, User.class); // may return null, as userStr = "null"
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        User user = null;
        if (users.size() > 0) user = users.get(0);

        // user may be null and stores as string "null" in redis, to avoid cache penetration issue
        redis.set(USERNAME_INFO + username, JsonUtils.objectToJson(user), USERNAME_INFO_TTL);

        return null;
    }

    @Override
    public User getUserById(String userId) {
        if (userId == null) return null;

        String userStr = redis.get(USERID_INFO + userId);

        if (StringUtils.isNotBlank(userStr)) {
            redis.expire(USERID_INFO + userId, USERID_INFO_TTL);
            return JsonUtils.jsonToPojo(userStr, User.class); // may return null, as userStr = "null"
        }

        User user = userMapper.selectByPrimaryKey(userId);
        redis.set(USERID_INFO + userId, JsonUtils.objectToJson(user), USERID_INFO_TTL);
        return user;
    }

    @Override
    public UserVO login(UserBO userBO,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        User user = getUserByUsername(username);
        if (user == null || !password.equals(MD5Utils.getMD5Str(user.getPassword()))) return null;

        String token = refreshToken(user.getUserId());

        UserVO userVO = convertUserToUserVO(user);
        CookieUtils.setCookie(request, response, TOKEN_COOKIE, token);
        CookieUtils.setCookie(request, response, USER_COOKIE, JsonUtils.objectToJson(userVO));
        return userVO;
    }

    @Override
    public void signup(UserBO userBO) throws Exception {
        User user = User.builder()
                .userId(snowFlake.nextSID())
                .isStar(YesOrNo.NO.value)
                .profileImg(DEFAULT_PROFILE_IMG_URL)
                .username(userBO.getUsername())
                .password(MD5Utils.getMD5Str(userBO.getPassword()))
                .build();

        redis.set(USERNAME_INFO + user.getUsername(), JsonUtils.objectToJson(user), USERNAME_INFO_TTL);
        redis.set(USERID_INFO + user.getUserId(), JsonUtils.objectToJson(user), USERID_INFO_TTL);

        kafka.send(INSERT_USER, JsonUtils.objectToJson(user));
    }

    @Override
    public boolean isUsernameExist(String username) {
        User user = getUserByUsername(username);
        return user != null;
    }

    @Override
    public void logout(UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        redis.del(USER_TOKEN + userBO.getUserId());
        CookieUtils.deleteCookie(request, response, TOKEN_COOKIE);
        CookieUtils.deleteCookie(request, response, USER_COOKIE);
    }

    private UserVO convertUserToUserVO(User user) {
        if (user == null) return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    private String refreshToken(String userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        redis.set(USER_TOKEN + userId, token, USER_TOKEN_TTL);
        return token;
    }
}
