package com.example.myproject.controller.RabbitMQ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.myproject.repository.Token;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Service;

@Service
public class ReceiveFromQueue {

    // @Autowired
    private SendToQueue send;
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    private static final Logger logger = LogManager.getLogger(ReceiveFromQueue.class);
    private final static String QUEUE_NAME = "validateTokenToBackend";

    public ReceiveFromQueue(SendToQueue send) throws IOException, TimeoutException {
        this.send = send;
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                try {
                    receiveToken();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                }
            }
        }).start();
    }

    // @Override
    // public void run(String... args) throws Exception {
    // send.sendToken(receiveToken());
    // }

    // @Async
    public void receiveToken() throws IOException, TimeoutException, InterruptedException {
        AtomicReference<String> tokenRef = new AtomicReference<>(null);
        CountDownLatch latch = new CountDownLatch(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String tokenMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            tokenRef.set(tokenMessage);
            logger.info(" [x] Received '" + tokenMessage + "'");
            latch.countDown();
        };

        String consumerTag = channel.basicConsume(QUEUE_NAME, true, deliverCallback, secondConsumerTag -> {
        });

        latch.await();

        String token = tokenRef.get();

        // Token validieren (z.B. mit einer JWT-Bibliothek)
        if (Token.isValidToken(token)) {
            send.sendToken(true);
        } else {
            send.sendToken(false);
            logger.warn("Token invalid:" + token);
        }
        channel.basicCancel(consumerTag);
    }
}
