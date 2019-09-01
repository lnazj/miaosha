package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-16
 */

public interface OrderService {
    // 1、通过前端传来的url上的秒杀活动id，校验id是否属于对应商品且活动已经开始，推荐使用
    //2. 根据商品判断是否存在秒杀活动，若存在则以秒杀价格下单
    OrderModel createOrder(String userId, String itemId, Integer amount,String promoId ) throws BusinessException;
}
