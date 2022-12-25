package com.korsuk.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;

public class ExchangeReceiver {

    private static final String EXCHANGE_NAME = "news_exchanger";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(Thread.currentThread().getName());
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        System.out.println("Channel is open:");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String command = sc.next();
            String[] splitCommand = command.split("/");

            switch (splitCommand[0]) {
                case "set_topic":
                    channel.queueBind(queueName, EXCHANGE_NAME, splitCommand[1]);
                    System.out.println(" [ * ] Waiting for messages with key {" + splitCommand[1] + "} :");
                    break;
                case "unset_topic":
                    channel.queueUnbind(queueName, EXCHANGE_NAME, splitCommand[1]);
                    System.out.println(" [ * ] Stopped writing messages with key {" + splitCommand[1] + "} :");
                    break;
            }
        }
        sc.close();

    }
}
