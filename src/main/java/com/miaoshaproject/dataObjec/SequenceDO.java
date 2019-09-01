package com.miaoshaproject.dataObjec;

import lombok.Data;

@Data
public class SequenceDO {

    private String name;
    private Integer currentValue;
    private Integer step;
}