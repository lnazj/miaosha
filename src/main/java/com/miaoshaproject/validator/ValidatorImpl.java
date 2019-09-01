package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import java.util.Set;


/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-07-15
 */
@Component
public class ValidatorImpl implements InitializingBean{
    private Validator validator;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 将hibernate validator 通过工厂的初始化方式使其实例化
        this.validator=Validation.buildDefaultValidatorFactory().getValidator();
    }

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult validationResult=new ValidationResult();
        final Set<ConstraintViolation<Object>> constraintViolationSet=validator.validate(bean);
        if(constraintViolationSet.size()>0){
            validationResult.setHasError(true);
            for(ConstraintViolation<Object> constraintViolation:constraintViolationSet){
                String errMsg=constraintViolation.getMessage();
                String propertyName=constraintViolation.getPropertyPath().toString();
                validationResult.getErrorMsgMap().put(propertyName,errMsg);
            }
        }
        return validationResult;
    }
}
