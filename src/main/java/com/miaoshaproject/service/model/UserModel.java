package com.miaoshaproject.service.model;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
@Data
public class UserModel implements Serializable {


    private String id;

    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotNull(message = "性别不能不填")
    private Byte gender;
    private Integer age;
    @NotNull(message = "手机号不能不填")
    private String telphone;
    private String thirdPartyId;
    @NotNull(message = "密码不能不填")
    private String encrptPassword;
    private String registerMode;
}
