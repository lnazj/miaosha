package com.miaoshaproject.dataObjec;

import lombok.Data;

import java.util.Date;

@Data
public class PromoDO {

    private String id;

    private String promoName;

    private Date startTiem;

    private String itemId;

    private Double promoItemPrice;

    private Date endDate;

}