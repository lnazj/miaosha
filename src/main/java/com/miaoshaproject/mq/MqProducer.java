package com.miaoshaproject.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-08-29
 */
@Component
public class MqProducer implements ConfirmCallback,ReturnCallback{


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    // 用于实现消息发送到Rabbitmq交换器后接受ack回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            System.out.println("消息发送成功"+correlationData);
        }else{
            System.out.println("消息发送失败"+cause);
        }
    }

    // 用于实现消息发送到Rabbitmq交换器,但无相应队列与交换器绑定时的回调。
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey){
        System.out.println(message.getMessageProperties().getCorrelationIdString() + " 发送失败");
    }

    // 发送消息
    public void send(String msg){
        CorrelationData correlationData=new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertSendAndReceive("topicExchange","key.1",msg,correlationData).toString();
    }
}
