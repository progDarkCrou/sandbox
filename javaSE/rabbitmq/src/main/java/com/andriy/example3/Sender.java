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

        ExecutorService executorService = Executors.newFixedThreadPool(count / 10);

        System.out.println("Started task sending...");
        int i = 0;
        while (i++ < count) {
            String replyQueueName = channel.queueDeclare().getQueue();
            String corrId = java.util.UUID.randomUUID().toString();
            AMQP.BasicProperties props = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();

            QueueingConsumer consumer = new QueueingConsumer(channel);

            channel.basicConsume(replyQueueName, consumer);

            final int finalI = i;

            results.add(executorService.submit(() -> {
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
