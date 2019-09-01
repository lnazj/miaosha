package com.miaoshaproject.error;

import com.miaoshaproject.response.CommonReturnType;
import org.omg.CORBA.UNKNOWN;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
public enum EmBusinessError implements CommonError {

    //通用错误类型10000开头
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),

    //20000表示用户信息相关错误
    USER_NOT_EXIST(20001,"用户不存在"),
    USR_LOGIN_FAIL(20002,"用户名或密码错误"),
    USR_UNLOGIN(20003,"用户未登录"),

    //30000表示为交易信息错误
    STOCK_NOT_ENOUGH(300001,"库粗不足"),
    RATELIMIT(30003,"活动太火爆");

    private EmBusinessError(int errCode,String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
       this.errMsg=errMsg;
       return this;
    }
}
