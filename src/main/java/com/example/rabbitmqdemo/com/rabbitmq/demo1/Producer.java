package com.example.rabbitmqdemo.com.rabbitmq.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @BelongsProject: rabbitmq-demo
 * @BelongsPackage: com.example.rabbitmqdemo.com.rabbitmq
 * @Author: SunHongWei
 * @Date: 2022/4/30 3:52 下午
 * @Description: TODO
 */
public class Producer {

    //队列名称
    public static final String QUEUE_NAME = "MESSAGE_QUEUE";

    //发消息
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        try {
            Connection connection = connectionFactory.newConnection();
            //创建信道
            Channel channel = connection.createChannel();
            /**
             * 声明队列
             * @param queue 队列名称
             * @param durable 是否持久化 默认存储在内存
             * @param exclusive 该队列是否提供一个消费者消费，是否进行共享 true，可以多个消费者消费
             * @param autoDelete 是否自动删除，最后一个消费者断开连接，是否 true自动删除
             * @param arguments 其他参数
             */
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = "{\"key\":\"value\"}";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
