package com.andriy.example3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by crou on 07.11.15.
 */
public class DellayedClient extends Client {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        int latency = (int) (Math.random() * 1000) * 2;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(com.andriy.example3.Sender.QUEUE_NAME, false, consumer);

        System.out.println("Started");
        System.out.println("Latency: " + latency);
        System.out.println("Waiting for messages...");

        while (true) {
            TimeUnit.MILLISECONDS.sleep(latency);
            work(channel, consumer);
        }
    }

}
