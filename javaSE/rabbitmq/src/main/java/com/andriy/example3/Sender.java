package com.andriy.example3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Created by avorona on 05.11.15.
 */
public class Sender {
    public static final String QUEUE_NAME = "rpc_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, CloneNotSupportedException {
        int count = 10000;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        LinkedList<Future<FibResult>> results = new LinkedList<>();

        System.out.println("Sender started");
        System.out.println("Start sending tasks...");

        int amount = 10000;
        while (num++ < amount) {
            final int finalNum = num;

            String replyQueueName = channel.queueDeclare().getQueue();
            String corrId = java.util.UUID.randomUUID().toString();

            AMQP.BasicProperties props = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();
            channel.basicPublish("", QUEUE_NAME, props, (finalNum + "").getBytes());
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(replyQueueName, true, consumer);

            Callable<BigInteger> task = () -> {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                while (!(delivery.getProperties().getCorrelationId().equals(corrId)))
                    delivery = consumer.nextDelivery();
                return new FibResult(new String(delivery.getBody()), finalI);
            }));

            channel.basicPublish("", QUEUE_NAME, props, ("" + finalI).getBytes());
//            executorService.submit(() -> {
//                try {
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
        }
        System.out.println(amount + " tasks sent");

        System.out.println("Listening for results...");
        results.parallelStream().forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
        channel.close();
        connection.close();
    }
}
