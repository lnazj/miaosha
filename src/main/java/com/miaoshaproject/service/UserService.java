package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */

public interface UserService {

    UserModel getUserById(String id);
    void regiestr(UserModel userModel)throws BusinessException;
    UserModel validateLogin(String telphone,String password)throws BusinessException;

    // 通过缓存获取用户对象
    UserModel getUserByCache(String userId);
}
