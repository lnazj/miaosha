package com.miaoshaproject.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-08-29
 */
@Component
public class MqConsumer {

    @RabbitListener(queues = "hello.queue1")
    public String processMessage1(String msg){
        System.out.println(Thread.currentThread().getName()+"接收到来自hello.queue1队列的消息");
        return msg.toUpperCase();
    }

    @RabbitListener(queues = "hello.queue2")
    public void processMessage2(String msg){
        System.out.println(Thread.currentThread().getName()+"接收到来自hello.queue2队列的消息");
    }
}
