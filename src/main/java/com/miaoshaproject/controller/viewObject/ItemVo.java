package com.miaoshaproject.controller.viewObject;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DateTimeException;

/**
 * 商品
 */
@Data
@NoArgsConstructor
public class ItemVo implements Serializable{

    private String id;

    // 商品名称
    private String title;

    //价格
    @Min(value = 0,message = "")
    private BigDecimal price;

    //商品库存
    private Integer stock;

    // 商品描述
    private String description;

    // 商品销量
    private Integer sales;

    // 图片
    private String imgUrl;

    // 0:没有 1 待开始 2进行中
    private Integer promoStatus;

    // 秒杀活动价格
    private BigDecimal promoPrice;

    private String promoId;

    private String startDate;

    private String endDate;

}
