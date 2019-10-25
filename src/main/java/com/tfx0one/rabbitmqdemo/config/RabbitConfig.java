package com.tfx0one.rabbitmqdemo.config;

import com.tfx0one.rabbitmqdemo.DemoVo;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述
 *
 * @author 2fx0one
 * @version 1.0
 * @createDate 2019-10-24 13:54
 * @projectName sprinboot-rabbit-mq-demo
 */
@EnableRabbit
@Configuration
public class RabbitConfig {
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        factory.setPrefetchCount(1);
//        factory.setMessageConverter(messageConverter);
//        return factory;
//    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setConnectionFactory(connectionFactory);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        //通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            System.out.println("消息唯一标识："+correlationData);
//            System.out.println("确认结果："+ack);
//            System.out.println("失败原因："+cause);
//        });
//        //通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            System.out.println("消息主体 message : "+message);
//            System.out.println("消息主体 message : "+replyCode);
//            System.out.println("描述："+replyText);
//            System.out.println("消息使用的交换器 exchange : "+exchange);
//            System.out.println("消息使用的路由键 routing : "+routingKey);
//        });
//        return rabbitTemplate;
//    }
}
