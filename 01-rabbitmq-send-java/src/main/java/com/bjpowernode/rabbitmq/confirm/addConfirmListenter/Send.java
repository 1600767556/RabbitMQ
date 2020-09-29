package com.bjpowernode.rabbitmq.confirm.addConfirmListenter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.174.131");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");

        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            //创建队列
            channel.queueDeclare("confirmQueue",true,false,false,null);
            /**
             * 声明一个劲交换机
             * 参数1 交换机的名称
             * 参数二  交换机的类型 direct fanout topic headers
             * 参数三 是否为持久化的交换机
             */
            channel.exchangeDeclare("directConfirmQueueExchange","direct",true);
            /**
             * 将队列绑定到交换机
             */
            channel.queueBind("confirmQueue","directConfirmQueueExchange","confirmRoutingkey");
            final String message = "普通发送者确认模式测试消息";
            /**
             * 发送消息到指定的队列
             */
            //开启事务
           // channel.txSelect();
            //启动发送者确认模式
            channel.confirmSelect();


            channel.addConfirmListener(new ConfirmListener() {
                //消息确认回调方法
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息被确认了 --- 消息编号: "+ deliveryTag + "  是否确认多条" +  multiple );



                }
                //消息没有确认的回调方法

                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                   System.out.println("消息没有被确认了 --- 消息编号: "+ deliveryTag + "  是否没有确认多条" +  multiple );
                }
            });

            for (int i = 0; i <10000 ; i++) {
                channel.basicPublish("directConfirmQueueExchange","confirmRoutingkey",null,message.getBytes());
            }

            //提交事务
            //channel.txCommit();
            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } /*finally {
            if (channel != null) {
                try {
                    //回滚事务
                    //channel.txRollback();
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
