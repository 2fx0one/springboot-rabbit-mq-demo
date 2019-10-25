package com.tfx0one.rabbitmqdemo.config;

import com.rabbitmq.client.Channel;
import com.tfx0one.rabbitmqdemo.DemoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
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
    public void defaultExchange(DemoVo demoVo, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        log.info("===== 1111111111111 receive " + demoVo.toString());
        try {
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    /**
     *  延迟队列的实现。 TTL + DLX
     *  为消息或者队列设置 TTL（time to live). 也就是过期时间，时间到了，该消息死亡。（三种死亡形式，1。被拒，2.TTL过期，3.队列最大长度）
     *  如果一个队列设置了Dead Letter Exchange （DLX) 如果到期，重新publish到Deal Letter Exchange 并通过 DLX routing路由到其他队列。
     *
     *  这里有个两种设置，如果既配置了消息的TTL，又配置了队列的TTL，那么较小的那个值会被取用。
     */

    /**
     * 这是第一种，消息有自己的独立事件。
    * */
    //第一步. 每条消息有自己的TTL，消息最初产生, 是投递在这个队列中的。只投递，不消费该消。
    // 这个队列使用默认的交换机绑定
    public static final String DELAY_QUEUE_PRE_MESSAGE_TTL = "DELAY_QUEUE_PRE_MESSAGE_TTL.1";

    //第二步. 消息一旦到期，转发到该DLX，由routing_key 派发到指定的处理队列 等待消费！
    public static final String DEAD_LETTER_PEOCESS_EXCHANGE_1 = "DEAD_LETTER_EXCHANGE.1";
    public static final String DEAD_LETTER_PROCESS_ROUTING_KEY = "DEAD_LETTER_PROCESS_ROUTING_KEY.1";
    public static final String DEAD_LETTER_PROCESS_QUEUE = "DEAD_LETTER_PROCESS_QUEUE.1";

    @Bean
    public org.springframework.amqp.core.Queue delayQueuePerMessageTTL() {
        //该队列绑定在默认交换机上。投递上来的消息。会带一个过期参数。过期后，会投递到对应的交换机上。
        return QueueBuilder.durable(DELAY_QUEUE_PRE_MESSAGE_TTL)
                // DLX，dead letter发送到的exchange
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_PEOCESS_EXCHANGE_1)
                // dead letter携带的routing key
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_PROCESS_ROUTING_KEY)
                .build();
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(DEAD_LETTER_PROCESS_QUEUE),
            //消息到期后, 投递到该交换机。并路由到指定队列
            exchange = @Exchange(DEAD_LETTER_PEOCESS_EXCHANGE_1),
            key = DEAD_LETTER_PROCESS_ROUTING_KEY
    ))
    public void delayMessageReceiver1(DemoVo demoVo) {
        log.info("===== 延迟队列。1 每条消息自己的单独设置 " + demoVo.toString());
    }



}
