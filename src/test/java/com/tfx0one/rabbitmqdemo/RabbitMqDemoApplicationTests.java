package com.tfx0one.rabbitmqdemo;

import com.tfx0one.rabbitmqdemo.config.DemoListener;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitMqDemoApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void test1_default_exchange() {
        System.out.println("11111");
        rabbitTemplate.convertAndSend(DemoListener.QUEUE_1, new DemoVo().setA("aa").setB(111));
    }

    @Test
    void test2_direct_exchange () {
        System.out.println("22222");
        System.out.println("DIRECT 模式 QUEUE_2 队列将收到");
        rabbitTemplate.convertAndSend(DemoListener.EXCHANGE_DIRECT_2, DemoListener.BINDING_KEY_2, new DemoVo().setA("bb").setB(222));
    }

    @Test
    void test3_topic_exchange () {
        System.out.println("333333");
        System.out.println("TOPIC模式 QUEUE_3_1 QUEUE_3_2 两个队列都将收到");
        rabbitTemplate.convertAndSend(DemoListener.EXCHANGE_TOPIC_3, "usa.news", new DemoVo().setA("cc").setB(333));
    }

    @Test
    void test4_fanout_exchange () {
        System.out.println("4444444");
        System.out.println("FANOUT 模式 QUEUE_4_1 QUEUE_4_2 两个队列都将收到");
        rabbitTemplate.convertAndSend(DemoListener.EXCHANGE_FANOUT_4, "", new DemoVo().setA("cc").setB(333));
    }

}
