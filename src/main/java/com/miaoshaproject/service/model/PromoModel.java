package com.miaoshaproject.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-17
 */
@Data
@NoArgsConstructor
public class PromoModel implements Serializable{
    private String id;

    // 秒杀活动状态 1 未开始 2进行中 3已结束
    private Integer status;
    // 秒杀活动名称
    private String promoName;

    // 秒杀活动开始时间
    private DateTime startDate;

    // 结束时间
    private DateTime endDate;
    // 秒杀商品
    private Integer itemId;

    // 秒杀活动商品价格
    private BigDecimal promoItemPrice;
}
