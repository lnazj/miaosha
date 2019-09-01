package com.miaoshaproject.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-16
 */
@RestController
@RequestMapping("/order")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private PromoService promoService;

    private ExecutorService excutorService;
    private RateLimiter orderCreateRateLimiter;

    // 初始化线程20个，拥塞窗口为20的等待队列-----泄洪
    public void init(){
        excutorService= Executors.newFixedThreadPool(20);
        orderCreateRateLimiter=RateLimiter.create(200);
    }


    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    public CommonReturnType generateSecondKillToken(@RequestParam(name="promoId")String promoId,
                                                    @RequestParam(name="itemId")String itemId,
                                                    @RequestParam(name="userId")String userId) throws BusinessException{

        // 验证用户是否登录
        String token=httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token)){
            throw new BusinessException(EmBusinessError.USR_UNLOGIN,"用户还未登录");
        }

        // 获取用户登录信息
        UserModel userModel=(UserModel) redisTemplate.opsForValue().get(token);
        if(userModel==null){
            throw new BusinessException(EmBusinessError.USR_UNLOGIN,"用户还未登录");
        }

        // 获取秒杀令牌
        String promoToken=promoService.generateSecondKillToken(promoId,itemId,userId);
        if(promoToken==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"生成令牌失败");
        }
        return CommonReturnType.create(promoToken);
    }

    @RequestMapping(value = "/createOrder",consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")String itemId,
                                        @RequestParam(name = "amount")Integer amount,
                                        @RequestParam(name = "promoId")String promoId,
                                        @RequestParam(name = "promoToken",required = false)String promoToken) throws BusinessException {

        if(orderCreateRateLimiter.acquire()<=0){
            throw new BusinessException(EmBusinessError.RATELIMIT);
        }
        String token=httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token)){
            throw new BusinessException(EmBusinessError.USR_UNLOGIN,"用户还未登录");
        }
         UserModel userModel=(UserModel) redisTemplate.opsForValue().get(token);
        if(userModel==null){
            throw new BusinessException(EmBusinessError.USR_UNLOGIN,"用户还未登录");
         }

        // 校验秒杀令牌是否正确
        if(promoId!=null){
            String inRedisPromoToken=(String)redisTemplate.opsForValue().get("promo_token_"+promoId+"_userId_"+userModel.getId()+"_itemId_"+itemId);
            if(inRedisPromoToken==null){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"令牌校验失败");
            }
            if(!org.apache.commons.lang3.StringUtils.equals(promoToken,inRedisPromoToken)){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"令牌校验失败");
            }
        }
        // session
        // Boolean isLogin=(Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");
//        if(isLogin==null|| !isLogin.booleanValue()){
//            throw new BusinessException(EmBusinessError.USR_UNLOGIN);
//        }
//        UserModel userModel=(UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        Future<Object> future=excutorService.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception{
                OrderModel orderModel=orderService.createOrder(userModel.getId(),itemId,amount,promoId);
                return null;
            }
        });
        return CommonReturnType.create(null);
    }
}

