package com.andriy.example3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by avorona on 05.11.15.
 */
public class Sender {
    public static final String QUEUE_NAME = "rpc_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        int num = 0;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Map<Integer, Future<BigInteger>> results = new HashMap<>();


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

                while (!delivery.getProperties().getCorrelationId().equals(corrId)) {
                    delivery = consumer.nextDelivery();
                }

                String res = new String(delivery.getBody());

                return new BigInteger(res);
            };

            results.put(num, executorService.submit(task));
        }
        System.out.println(amount + " tasks sent");

        executorService.shutdown();

        float start = System.nanoTime();

        results.entrySet().parallelStream().forEach(res -> {
            try {
                System.out.println("Result: fib(" + res.getKey() + ") = " + res.getValue().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

        float delta = System.nanoTime() - start;

        System.out.println("Process take: " + Duration.ofNanos((long) delta).getSeconds() + "s");

        channel.close();
        connection.close();
    }
}
