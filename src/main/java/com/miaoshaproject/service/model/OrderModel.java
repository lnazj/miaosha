package com.miaoshaproject.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单模型
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-16
 */
@Data
public class OrderModel {
    //订单号
    private String id;

    // 用户id
    private String userId;

    //购买的商品id
    private String itemId;

    //购买数量
    private Integer amount;

    // 若非空，表示以秒杀方式下单
    private String promoId;

    // 购买时的单价,因为价格会变，若promoId非空，表示秒杀商品价格
    private BigDecimal itemPrice;

    // 购买金额，若promoId非空，表示秒杀商品价格
    private BigDecimal orderPrice;
}
