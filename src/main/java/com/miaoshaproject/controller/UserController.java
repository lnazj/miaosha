package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewObject.UserVo;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.alibaba.druid.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
@RestController("user")
@RequestMapping(value="/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController{
    @Autowired
    private UserService userService;

    // httpservletRequest 让用户在自己的线程中做处理
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    /**
     * 用户登录
     * @param telphone
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws BusinessException
     */
    @ResponseBody
    @PostMapping(value="/validateLogin",consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType validateLogin(@RequestParam(name="telphone") String telphone,
                                    @RequestParam(name="password") String password)throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException{
        //入参校验，不能为空
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 用户登录密码是否正确
        UserModel userModel=userService.validateLogin(telphone, EncodeByMd5(password));

        //基于cookie传输session方式-————将登陆凭证加入到用户登录成功的session内,放到了redis内
//        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
//        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        // token 认证
        String uuidToken=UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuidToken,userModel,1, TimeUnit.MINUTES);

        return CommonReturnType.create(uuidToken);
    }

    /**
     * 用户注册
     * @param telphone
     * @param otpCode
     * @param name
     * @param gender
     * @param password
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @PostMapping(value="/registr",consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType registr(@RequestParam(name="telphone") String telphone,
                                      @RequestParam(name="otpCode")String otpCode,
                                      @RequestParam(name="name")String name,
                                      @RequestParam(name = "gender")Integer gender,
                                      @RequestParam(name = "password")String password)throws BusinessException,UnsupportedEncodingException, NoSuchAlgorithmException{
        //验证手机号和对应的optioncode是否相符
        String inSessionoptCode= (String) httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(inSessionoptCode,otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码失败");
        }

        //进行用户信息验证
        UserModel userModel=new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setTelphone(telphone);
        userModel.setEncrptPassword(EncodeByMd5(password));
        userModel.setRegisterMode("byphone");
        userService.regiestr(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 密码加密
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException,UnsupportedEncodingException{
        MessageDigest digest=MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();
        String newstr=base64Encoder.encode(digest.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * 根据手机号获取验证码
     * @param telphone
     * @return
     */
    @PostMapping(value={"/getotp/{telphone}"})
    public CommonReturnType getotp(@PathVariable String telphone){
        // 1.生成验证码
        Random random=new Random();
        int randomInt=random.nextInt(99999);
        randomInt+=10000;
        String otpCode=String.valueOf(randomInt);

        // 2.todo 通过redis接入，反复点击反复覆盖，将opt和用户收计划相关联，现在通过httpsession的方式绑定
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

        // 3.将OPT验证码通过短信发送给用户
        System.out.println("telephone"+telphone+"Code"+otpCode);
        return CommonReturnType.create(null);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     * @throws BusinessException
     */
    @GetMapping(value = {"/getUser/{id}"})
   public CommonReturnType getUser(@PathVariable(value = "id") String id) throws BusinessException{
        UserModel userModel=userService.getUserById(id);
        if(userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return CommonReturnType.create(userVo);
   }
}
