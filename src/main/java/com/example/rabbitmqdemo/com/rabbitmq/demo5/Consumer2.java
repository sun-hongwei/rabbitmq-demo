package com.example.rabbitmqdemo.com.rabbitmq.demo5;

import com.example.rabbitmqdemo.com.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @BelongsProject: rabbitmq-demo
 * @BelongsPackage: com.example.rabbitmqdemo.com.rabbitmq
 * @Author: SunHongWei
 * @Date: 2022/4/30 4:38 下午
 * @Description: fanout 交换机
 */
public class Consumer2 {

    //队列名称
    private final static String QUEUE_NAME = "EXCHANGE_QUEUE_FANOUT_2";
    //交换机名称
    public static final String EXCHANGE_NAME = "EXCHANGE-FANOUT";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //将交换机与队列绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            /**
             *  * @param deliveryTag 消息唯一ID
             *  * @param multiple false 不批量应答消息
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println("消息内容" + message + " \r\n消息ID：" + delivery.getEnvelope().getDeliveryTag());
        };
        //取消消费的一个回调接口如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息消费被中断");
        };

        /*消费者消费消息
         *1.消费哪个队列
         *2.消费成功之后是否要自动应答true代表自动应答false手动应答
         *3.消费者未成功消费的回调
         */
        System.out.println("二号工作线程");
        //手动应答
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
