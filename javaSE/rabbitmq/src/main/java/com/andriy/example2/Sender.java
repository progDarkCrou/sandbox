package com.andriy.example2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by crou on 04.11.15.
 */
public class Sender {

    public static String QEUE_NAME = "durable_queue_3";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;
        channel.queueDeclare(QEUE_NAME, durable, false, false, null);
        channel.basicPublish("", QEUE_NAME, MessageProperties.TEXT_PLAIN,
                "Hello messsage".getBytes());

        channel.close();
        connection.close();
    }
}
