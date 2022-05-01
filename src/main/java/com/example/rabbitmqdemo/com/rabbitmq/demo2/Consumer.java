package com.example.rabbitmqdemo.com.rabbitmq.demo2;

import com.example.rabbitmqdemo.com.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @BelongsProject: rabbitmq-demo
 * @BelongsPackage: com.example.rabbitmqdemo.com.rabbitmq
 * @Author: SunHongWei
 * @Date: 2022/4/30 4:38 下午
 * @Description: 轮询接收消息
 */
public class Consumer {

    private final static String QUEUE_NAME = "MESSAGE_QUEUE";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("消息内容" + message);
        };
        //取消消费的一个回调接口如在消费的时候队列被删除掉了
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息消费被中断");
        };

        /*消费者消费消息
         *1.消费哪个队列
         *2.消费成功之后是否要自动应答 true代表自动应答 false手动应答
         *3.消费者未成功消费的回调
         */
        System.out.println("二号工作线程");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
