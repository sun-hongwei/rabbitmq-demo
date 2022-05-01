package com.example.rabbitmqdemo.com.rabbitmq.demo3;

import com.example.rabbitmqdemo.com.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;

/**
 * @BelongsProject: rabbitmq-demo
 * @BelongsPackage: com.example.rabbitmqdemo.com.rabbitmq
 * @Author: SunHongWei
 * @Date: 2022/4/30 3:52 下午
 * @Description: ACK 确认消息
 */
public class Producer {

    //队列名称
    public static final String QUEUE_NAME = "ACK_MESSAGE_QUEUE";

    //发消息
    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            /**
             * 声明队列
             * @param queue 队列名称
             * @param durable 是否持久化 默认存储在内存
             * @param exclusive 该队列是否提供一个消费者消费，是否进行共享 true，可以多个消费者消费
             * @param autoDelete 是否自动删除，最后一个消费者断开连接，是否 true自动删除
             * @param arguments 其他参数
             */
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            //从控制台接收消息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String next = scanner.next();
                //MessageProperties.PERSISTENT_TEXT_PLAIN (消息持久化)
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, next.getBytes());
                System.out.println("发送成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
