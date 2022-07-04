package com.ziyuan.service;

import com.ziyuan.pojo.User;
import com.ziyuan.pojo.bo.UserBO;
import com.ziyuan.pojo.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface UserService {

    public void signup(UserBO userBO) throws Exception;

    public User getUserByUsername(String username);

    public User getUserById(String id);

    public UserVO getUserVOById(String id);

    public UserVO getUserVOByUsername(String username);

    public boolean auth(String userId, String token, HttpServletRequest request, HttpServletResponse response) throws Exception;

    public UserVO login(UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception;

    public boolean isUsernameExist(String username);

    public void logout(UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
