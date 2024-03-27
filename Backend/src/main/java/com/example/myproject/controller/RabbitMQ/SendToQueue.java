package com.example.myproject.controller.RabbitMQ;

import com.rabbitmq.client.ConnectionFactory;

import io.jsonwebtoken.io.IOException;

import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import jakarta.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.Logger;

@Service
public class SendToQueue {

    private static final Logger logger = LogManager.getLogger(SendToQueue.class);
    private final static String QUEUE_NAME = "validateTokenToStreaming";

    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;

    public SendToQueue() throws IOException, TimeoutException, java.io.IOException {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void sendToken(boolean bol) throws IOException, java.io.IOException {
        String validated = String.valueOf(bol);
        channel.basicPublish("", QUEUE_NAME, null, validated.getBytes(StandardCharsets.UTF_8));
        logger.info(" [x] Sent '" + validated + "'");
    }

    @PreDestroy
    public void cleanUp() throws IOException, TimeoutException, java.io.IOException {
        channel.close();
        connection.close();
    }
}
