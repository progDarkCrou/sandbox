package com.andriy.example1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by crou on 04.11.15.
 */
public class Sender {

    public static String QEUE_NAME = "message_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QEUE_NAME, false, false, false, null);
        channel.basicPublish("", QEUE_NAME, null, "Hello messsage".getBytes());

        channel.close();
        connection.close();
    }
}
