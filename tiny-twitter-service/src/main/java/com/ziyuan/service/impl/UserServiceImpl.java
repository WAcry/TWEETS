package com.ziyuan.service.impl;

import com.ziyuan.mapper.UserMapper;
import com.ziyuan.pojo.User;
import com.ziyuan.pojo.UserExample;
import com.ziyuan.pojo.bo.UserBO;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.KafkaOperator;
import com.ziyuan.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Sid sid;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private KafkaOperator kafkaOperator;

    @Override
    public void signup(UserBO userBO) {
        User user = new User();
        user.setId(sid.nextShort());
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String profile = userBO.getProfileImg();
        user.setProfileImg(StringUtils.isBlank(profile)
                ? "https://raw.githubusercontent.com/Quakiq/tinyimages/main/img/202205242146238.png"
                : profile);

        kafkaOperator.send("INSERT_USER", JsonUtils.objectToJson(user));
    }

    public User getUserByUsername(String usrname) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(usrname);

        List<User> usrs = userMapper.selectByExample(example);
        if (usrs != null && usrs.size() > 0) {
            return usrs.get(0);
        }
        return null;
    }

    @Override
    public void login(UserBO userBO) {
        System.out.println("login");
    }
}
