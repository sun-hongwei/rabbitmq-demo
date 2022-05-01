package com.example.rabbitmqdemo.com.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @BelongsProject: rabbitmq-demo
 * @BelongsPackage: com.example.rabbitmqdemo.com.rabbitmq.util
 * @Author: SunHongWei
 * @Date: 2022/4/30 5:33 下午
 * @Description: TODO
 */
public class RabbitMqUtils {

    public static Channel getChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
