package com.tfx0one.rabbitmqdemo;

import com.tfx0one.rabbitmqdemo.config.DemoListener;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
class RabbitMqDemoApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void test1_default_exchange() {
        IntStream.rangeClosed(1, 5).forEach(i->{
            rabbitTemplate.convertAndSend(DemoListener.QUEUE_1, new DemoVo().setA("aa").setB(i));
            try {
                new Thread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Message msg = rabbitTemplate.receive(DemoListener.QUEUE_1,1000);
//            System.out.println(msg);
            System.out.println("默认交换机 QUEUE_1将收到 " + i);
        });
    }

    @Test
    void test2_direct_exchange () {
        System.out.println("22222");
        rabbitTemplate.convertAndSend(DemoListener.EXCHANGE_DIRECT_2, DemoListener.BINDING_KEY_2, new DemoVo().setA("bb").setB(222));
        System.out.println("DIRECT 模式 QUEUE_2 队列将收到");
    }

    @Test
    void test3_topic_exchange () {
        System.out.println("333333");
        rabbitTemplate.convertAndSend(DemoListener.EXCHANGE_TOPIC_3, "usa.news", new DemoVo().setA("cc").setB(333));
        System.out.println("TOPIC模式 QUEUE_3_1 QUEUE_3_2 两个队列都将收到");
    }

    @Test
    void test4_fanout_exchange () {
        System.out.println("4444444");
        rabbitTemplate.convertAndSend(DemoListener.EXCHANGE_FANOUT_4, "", new DemoVo().setA("cc").setB(4444));
        System.out.println("FANOUT 模式 QUEUE_4_1 QUEUE_4_2 两个队列都将收到");
    }

    @Test
    void test5_dead_letter_exchange () {
        System.out.println("5555555");
        //投递到默认交换机上的队列 DELAY_QUEUE_PRE_MESSAGE_TTL 该队列设置的 DLX
        rabbitTemplate.convertAndSend(DemoListener.DELAY_QUEUE_PRE_MESSAGE_TTL, new DemoVo().setA("cc").setB(5555), message -> {
            message.getMessageProperties().setExpiration(String.valueOf(10*1000));
            return message;
        });
        System.out.println("TTL_DLX 模式 DEAD_LETTER_PROCESS_QUEUE.1 将延时收到");
    }
}
