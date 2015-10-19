package com.vsa.checker.services;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.vsa.checker.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by avorona on 15.10.15.
 */
@Component
@Scope("singleton")
public class MessageReceiver extends DefaultConsumer {

    @Autowired
    public MessageReceiver(@Qualifier("receivingChannel") Channel receivingChannel) throws IOException {
        super(receivingChannel);
        receivingChannel.queueDeclare(Application.QUEUE_NAME, false, false, false, null);
        receivingChannel.basicConsume(Application.QUEUE_NAME, false, this);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println(new String(body, "UTF-8"));
    }
}
