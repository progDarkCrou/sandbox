package com.andriy.example2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by crou on 04.11.15.
 */
public class Client {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

//        channel.queueDeclare(com.andriy.example2.Sender.QEUE_NAME, true, false, false, null);
        channel.basicQos(1);
        channel.basicConsume(com.andriy.example2.Sender.QEUE_NAME, false, new DefaultConsumer(channel) {

            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("Received message...");
                try {
                    TimeUnit.SECONDS.sleep((long) (Math.random() * 5));
                    if (new Random().nextInt(2) == 1) {
                        System.out.println(new String(body));
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } else {
//                        channel.basicNack(envelope.getDeliveryTag(), false, false);
                        channel.close();
                        connection.close();
                        throw new RuntimeException("Exiting with no result");
                    }
                } catch (InterruptedException e) {
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
