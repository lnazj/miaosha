package com.miaoshaproject.controller.viewObject;

import lombok.Data;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
@Data
public class UserVo {
    private String id;
    private String name;
    private Byte gender;
    private Integer age;
    private String telphone;
}
