package com.miaoshaproject;

import com.miaoshaproject.mq.MqProducer;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author 李娜
 * @version 0.0.1
 * @since 0.0.1  2019-08-29
 */
@RunWith(value=SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= Application.class)
public class RabbitTests {

    @Autowired
    private MqProducer sender;

    @Test
    public void sendTest() throws Exception{
        while(true){
            String msg=new Date().toString();
            sender.send(msg);
        }
    }
}
