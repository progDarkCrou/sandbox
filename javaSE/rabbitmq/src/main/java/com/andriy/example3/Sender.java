package com.andriy.example3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by avorona on 05.11.15.
 */
public class Sender {
    public static String QUEUE_NAME = "rpc_queue_1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        String replyQueueName = channel.queueDeclare().getQueue();

        channel.basicConsume(replyQueueName, true, consumer);

        String corrId = java.util.UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(corrId)
                .contentEncoding("UTF-8")
                .replyTo(replyQueueName)
                .build();

        String numb = "10";
        channel.basicPublish("", QUEUE_NAME, props, numb.getBytes());

        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

        while (!delivery.getProperties().getCorrelationId().equals(corrId)) {
            delivery = consumer.nextDelivery();
        }

        System.out.println("Result: fib(" + numb + ") = " + new String(delivery.getBody()));

        channel.close();
        connection.close();
    }
}
