package com.miaoshaproject.dataObjec;

import lombok.Data;

@Data
public class OrderDo {
    private String id;

    private String userId;

    private String itemId;

    // 若非空，表示以秒杀方式下单
    private String promoId;

    private Double itemPrice;

    private Integer amount;

    private Double orderPrice;
}