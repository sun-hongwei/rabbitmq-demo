package com.example.rabbitmqdemo.com.rabbitmq.demo6;

import com.example.rabbitmqdemo.com.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @BelongsProject: rabbitmq-demo
 * @BelongsPackage: com.example.rabbitmqdemo.com.rabbitmq
 * @Author: SunHongWei
 * @Date: 2022/4/30 3:52 下午
 * @Description: DIRECT 交换机
 */
public class Producer {

    //交换机名称
    public static final String EXCHANGE_NAME = "EXCHANGE-DIRECT";

    //发消息
    public static void main(String[] args) throws InterruptedException {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            //开启消息发布确认
            channel.confirmSelect();
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            //从控制台接收消息
            Scanner scanner = new Scanner(System.in);
            /**
             * 线程安全有序的一个哈希表，适用于高并发的情况
             * 1.轻松的将序号与消息进行关联
             * 2.轻松批量删除条目只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap();

            //3.2成功消息回调函数 deliveryTag：消息唯一标识 multiple：是否批量确认
            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
                System.out.println("生产者消息ID:" + deliveryTag);
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap =
                            concurrentSkipListMap.headMap(deliveryTag);
                    longStringConcurrentNavigableMap.clear();
                } else {
                    concurrentSkipListMap.remove(deliveryTag);
                }
            };
            //3.3失败消息回调函数
            ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
                String s = concurrentSkipListMap.get(deliveryTag);
                System.out.println("未确认的消息为：" + s);
            };
            //3.1准备消息监听器，监听消息成功与失败，失败消息需要重新发送
            channel.addConfirmListener(ackCallback, nackCallback);

            while (scanner.hasNext()) {
                String next = scanner.next();
                //MessageProperties.PERSISTENT_TEXT_PLAIN (消息持久化)
                channel.basicPublish(EXCHANGE_NAME, "info", MessageProperties.PERSISTENT_TEXT_PLAIN, next.getBytes());
                //1、单个消息发布，等待确认
                //boolean b = channel.waitForConfirms();
                //System.out.println((b == true) ? "确认完成" : "确认失败");
                //2、批量消息发布，等待确认
                //同channel.waitForConfirms();
                //3、异步发布确认↑
                //记录所有的消息 channel.getNextPublishSeqNo() 获取下一个消息的ID值
                concurrentSkipListMap.put(channel.getNextPublishSeqNo(), next);
                System.out.println("发送成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
