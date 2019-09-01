package com.miaoshaproject.error;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-13
 */
//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{
    private CommonError commonError;

    // 直接接收EmBusinessError的传参用户构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }

    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
