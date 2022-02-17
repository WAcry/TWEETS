package com.ziyuan.controller;

import com.ziyuan.pojo.User;
import com.ziyuan.pojo.bo.UserBO;
import com.ziyuan.pojo.vo.UserVO;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.CookieUtils;
import com.ziyuan.utils.JSONResult;
import com.ziyuan.utils.MD5Utils;
import com.ziyuan.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redis;

    @PostMapping("/signup")
    public JSONResult signup(@RequestBody UserBO userBO) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // Checks:
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("username and password must not be null");
        }
        if (username.length() > 30) {
            return JSONResult.errorMsg("username can be max 30 chars");
        }
        if (password.length() < 6) {
            return JSONResult.errorMsg("password must be at least 6 characters");
        }

        User user = userService.getUserByUsername(username);
        if (user != null) {
            return JSONResult.errorMsg("username has been registered");
        }

        userService.signup(userBO);
        return JSONResult.ok();
    }


    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("username and password must not be null");
        }

        User user = userService.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(MD5Utils.getMD5Str(password))) {
            return JSONResult.errorMsg("username or password is incorrect");
        }

        String token = UUID.randomUUID().toString();
        redis.set("token:" + user.getId(), token, 60 * 60 * 24 * 30);

        CookieUtils.setCookie(request, response, "token", token);
        CookieUtils.setCookie(request, response, "userId", user.getId());

        return JSONResult.ok(ConvertToUserVO(user));
    }

    @PostMapping("logout")
    public JSONResult logout(
            @RequestBody UserBO userBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (!auth(userBO.getUserId(), userBO.getToken())) {
            return JSONResult.errorMsg("already logout before");
        }

        redis.del("token:" + userBO.getUserId());
        CookieUtils.deleteCookie(request, response, "token");
        CookieUtils.deleteCookie(request, response, "userId");

        return JSONResult.ok();
    }

    private UserVO ConvertToUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserId(user.getId());
        return userVO;
    }
}
