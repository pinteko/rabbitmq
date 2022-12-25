package com.korsuk.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ExchangeSender {

    private static final String EXCHANGE_NAME = "news_exchanger";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
//            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String message = "java some message";

            String theme = message.split(" ")[0];

//            channel.basicPublish(EXCHANGE_NAME, "programming.best-practices.java", null, message.getBytes("UTF-8"));
            channel.basicPublish(EXCHANGE_NAME, theme, null, message.getBytes("UTF-8"));
            System.out.println("Send: '" + message + "'");
        }
    }
}
