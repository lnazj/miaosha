package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataObjec.PromoDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.service.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-17
 */
@Service
public class PromoServiceImpl implements PromoService{
    @Autowired
    private PromoDOMapper promoDoMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public PromoModel getPromoByItemId(String itemId){
        PromoDO promoDo= promoDoMapper.selectByItemId(itemId);
        PromoModel promoModel=convertFromDo(promoDo);
        if (promoModel==null){
            return null;
        }
        // 判断当前时间秒杀活动是否开始
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        return promoModel;
    }

    private PromoModel convertFromDo(PromoDO promoDo){
        if (promoDo == null) {
            return null;
        }
        PromoModel promoModel=new PromoModel();
        BeanUtils.copyProperties(promoDo,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDo.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDo.getStartTiem()));
        promoModel.setEndDate(new DateTime(promoDo.getEndDate()));
        return promoModel;
    }

    @Override
    public void publishPromo(String promoId) {
       PromoDO promoDO= promoDoMapper.selectByPrimaryKey(promoId);
       if(promoDO.getItemId()==null){
           return;
       }
       ItemModel itemModel=itemService.getItemById(promoDO.getItemId());

       // 将库存存入redis
       redisTemplate.opsForValue().set("promo_item_stock_"+itemModel.getId(),itemModel.getStock());

       // 将大闸的限制数字设到redis内
       redisTemplate.opsForValue().set("promo_door_count"+promoId,itemModel.getStock().intValue()*5);
    }

    @Override
    public String generateSecondKillToken(String promoId,String itemId,String userId) {
        PromoDO promoDo= promoDoMapper.selectByPrimaryKey(promoId);
        PromoModel promoModel=convertFromDo(promoDo);
        if (promoModel==null){
            return null;
        }

        // 判断当前时间秒杀活动是否开始
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        if(promoModel.getStatus().intValue()!=2){
            return null;
        }

        ItemModel itemModel=itemService.getItemByIdInCache(itemId);
        if (itemModel==null){
           return null;
        }

        UserModel userModel=userService.getUserByCache(userId);
        if (userModel == null) {
            return null;
        }

        long result=redisTemplate.opsForValue().increment("promo_door_count"+promoId,-1);
        if(result>=0){
            String token= UUID.randomUUID().toString().replace("-","");
            redisTemplate.opsForValue().set("promo_token_"+promoId+"_userId_"+userId+"_itemId_"+itemId,token,5, TimeUnit.MINUTES);
            return token;
        }else {
            return null;
        }
    }
}
