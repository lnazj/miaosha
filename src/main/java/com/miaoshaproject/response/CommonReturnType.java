package com.miaoshaproject.response;

import lombok.Data;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
@Data
public class CommonReturnType implements Serializable {

    // 表示返回请求结果"success"或"fail
    private String status;

    // 若status=success data返回前端需要的json
    // 若status=fail data内使用通用的错误码格式
    private Object data;

    // 定义一个通用的方法
    public static CommonReturnType create(Object result){

        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type=new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
