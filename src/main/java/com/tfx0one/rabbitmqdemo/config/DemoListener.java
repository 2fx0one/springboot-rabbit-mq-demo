package com.tfx0one.rabbitmqdemo.config;

import com.rabbitmq.client.Channel;
import com.tfx0one.rabbitmqdemo.DemoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 描述
 *
 * @author 2fx0one
 * @version 1.0
 * @createDate 2019-10-24 13:57
 * @projectName sprinboot-rabbit-mq-demo
 */
@Component
@Slf4j
public class DemoListener {

    public final static String QUEUE_1 = "QUEUE_1";

    @RabbitListener(queuesToDeclare = @Queue(QUEUE_1))
    public void defaultExchange(DemoVo demoVo, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        log.info("===== 1111111111111 receive " + demoVo.toString());
        channel.basicAck(deliveryTag, true);
    }


    /**
     * 默认模式 点对点 ExchangeTypes.DIRECT
     * 声明一个绑定，绑定一个队列和一个交换机，绑定关系 binding key,
     * 当一个消息Message中的路由键(routing key) 如何和binding key，交换机就把消息发送到对应队列。最后由该listener监听。
     * 由于是 direct 模式， routing key 和 binding key 是完全匹配的模式。且是单播的。
     */
    public final static String QUEUE_2 = "QUEUE_2"; //只用于声明队列
    public final static String EXCHANGE_DIRECT_2 = "exchange_2";
    public final static String BINDING_KEY_2 = "routing_key_2"; // 同时也是 routing key

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(QUEUE_2),
            exchange = @Exchange(value = EXCHANGE_DIRECT_2, type = ExchangeTypes.DIRECT),
            key = BINDING_KEY_2
    ))
    public void process(DemoVo demoVo, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        log.info("===== 22222222 receive " + demoVo.toString());
        channel.basicAck(deliveryTag, true);
    }

    /**
     * topic 模式 订阅模式 ExchangeTypes.TOPIC
     * 路由键 模式匹配。单词间用 . 分割。 * 匹配一个单词。 #匹配0或多个单词
     */
    public final static String QUEUE_3_1 = "QUEUE_3_1"; //只用于声明队列
    public final static String QUEUE_3_2 = "QUEUE_3_2"; //只用于声明队列
    public final static String EXCHANGE_TOPIC_3 = "exchange_top_3";
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(QUEUE_3_1),
            exchange = @Exchange(value = EXCHANGE_TOPIC_3, type = ExchangeTypes.TOPIC),
            key = "usa.#"
    ))
    public void topicReceiver1(DemoVo demoVo) {
        log.info("===== 3333333333333.1 receive " + demoVo.toString());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(QUEUE_3_2),
            exchange = @Exchange(value = EXCHANGE_TOPIC_3, type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void topicReceiver2(DemoVo demoVo) {
        log.info("===== 3333333333333.2 receive " + demoVo.toString());
    }


    /**
     * topic 模式 订阅模式 ExchangeTypes.TOPIC
     * 路由键 模式匹配。单词间用 . 分割。 * 匹配一个单词。 #匹配0或多个单词
     */
    public final static String QUEUE_4_1 = "QUEUE_4_1"; //只用于声明队列
    public final static String QUEUE_4_2 = "QUEUE_4_2"; //只用于声明队列
    public final static String EXCHANGE_FANOUT_4 = "exchange_fanout_4";
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(QUEUE_4_1),
            exchange = @Exchange(value = EXCHANGE_FANOUT_4, type = ExchangeTypes.FANOUT)
    ))
    public void fanoutReceiver1(DemoVo demoVo) {
        log.info("===== 44444444444444.1 receive " + demoVo.toString());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(QUEUE_4_2),
            exchange = @Exchange(value = EXCHANGE_FANOUT_4, type = ExchangeTypes.FANOUT)
    ))
    public void fanoutReceiver2(DemoVo demoVo) {
        log.info("===== 44444444444444.2 receive " + demoVo.toString());
    }
}
