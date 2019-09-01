package com.miaoshaproject.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.miaoshaproject.dao.UserDoMapper;
import com.miaoshaproject.dao.UserPasswordDoMapper;
import com.miaoshaproject.dataObjec.UserDo;
import com.miaoshaproject.dataObjec.UserPasswordDo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDoMapper userDaoMapper;

    @Resource
    private UserPasswordDoMapper userPasswordDaoMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public UserModel validateLogin(String telphone, String password) throws BusinessException {
        UserModel userModel = userDaoMapper.selectByTelphone(telphone, password);
        if (ObjectUtils.isEmpty(userModel)) {
            throw new BusinessException(EmBusinessError.USR_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public UserModel getUserById(String id) {
        UserDo userDao = userDaoMapper.selectByPrimaryKey(id);
        if (userDao == null) {
            return null;
        }

        UserPasswordDo userPasswrodDao = userPasswordDaoMapper.selectByUserId(userDao.getId());
        if (userPasswrodDao == null) {
            return null;
        }
        return convertFromDataObject(userDao, userPasswrodDao);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regiestr(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(userModel.getName()==null||
//           StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }

        ValidationResult result = validator.validate(userModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        UserDo userDao = new UserDo();
        BeanUtils.copyProperties(userModel, userDao);
        userDao.setId(IdWorker.getIdStr());
        try {
            userDaoMapper.insertSelective(userDao);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号重复");
        }

        UserPasswordDo userPasswordDao = new UserPasswordDo();
        BeanUtils.copyProperties(userModel, userPasswordDao);
        userPasswordDao.setUserId(userDao.getId());
        userPasswordDao.setId(IdWorker.getIdStr());
        userPasswordDaoMapper.insertSelective(userPasswordDao);
    }

    private UserModel convertFromDataObject(UserDo userDao, UserPasswordDo userPasswrodDao) {
        if (userDao == null || userPasswrodDao == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDao, userModel);
        userModel.setEncrptPassword(userPasswrodDao.getEncrptPassword());
        return userModel;
    }

    @Override
    public UserModel getUserByCache(String userId) {
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get("item_validate_" + userId);
        if (userModel == null) {
            userModel = this.getUserById(userId);
            redisTemplate.opsForValue().set("item_validate_" + userId, userModel, 10, TimeUnit.MINUTES);
        }
        return userModel;
    }
}
