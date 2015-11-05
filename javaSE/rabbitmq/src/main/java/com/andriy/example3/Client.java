package com.andriy.example3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * Created by avorona on 05.11.15.
 */
public class Client {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicQos(1);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(com.andriy.example3.Sender.QUEUE_NAME, false, consumer);

        System.out.println("Waiting for messages...");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            AMQP.BasicProperties props = delivery.getProperties();

            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(props.getCorrelationId())
                    .build();

            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println("calculating fib(" + message + ")...");

            String resp = "" + fib(new BigInteger(message));

            System.out.println("Responsing...");
            channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, resp.getBytes());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println("Sent...");
        }
    }

    public static BigInteger fibT(BigInteger times) {
        float start = System.currentTimeMillis();
        BigInteger res = fib(times);
        float delta = System.currentTimeMillis() - start;
        System.out.println("fib(" + times.toString() + ") takes: " +
                Duration.ofMillis((long) delta).getSeconds() + "s");
        return res;
    }

    public static BigInteger fib(BigInteger times) {
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;

        while ((times = times.subtract(BigInteger.ONE)).compareTo(BigInteger.ZERO) != 0) {
            BigInteger t = a;
            a = new BigInteger(b.toByteArray());
            b = b.add(t);
        }

        return b;
    }

}
