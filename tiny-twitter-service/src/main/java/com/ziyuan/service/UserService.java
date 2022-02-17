package com.ziyuan.service;

import com.ziyuan.pojo.User;
import com.ziyuan.pojo.bo.UserBO;

public interface UserService {

    public void signup(UserBO userBO);

    public User getUserByUsername(String usrname);

    public void login(UserBO userBO);

}
