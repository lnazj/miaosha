package com.miaoshaproject.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-08-29
 */
@Configuration
public class RabbitConfig {

    // 队列
    @Bean
    public Queue queue1(){
        return new Queue("hello queue1",true);
    }

    @Bean
    public Queue queue2(){
        return new Queue("hello queue2",true);
    }

    // 交换器
    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange("topicExchangeTest");
    }

    //绑定
    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(queue1()).to(topicExchange()).with("key.1");
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(queue2()).to(topicExchange()).with("key.#");
    }
}
