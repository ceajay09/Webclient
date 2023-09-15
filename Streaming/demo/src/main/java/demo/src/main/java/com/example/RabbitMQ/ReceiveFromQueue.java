package demo.src.main.java.com.example.RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ReceiveFromQueue {

    private static final Logger logger = LogManager.getLogger(ReceiveFromQueue.class);

    private final static String QUEUE_NAME = "validateTokenToStreaming";

    public boolean receiveTokenResponse() throws IOException, TimeoutException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        AtomicReference<String> tokenResponseRef = new AtomicReference<>(null);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String tokenResponse = new String(delivery.getBody(), StandardCharsets.UTF_8);
            tokenResponseRef.set(tokenResponse);
            logger.info(" [x] Received '" + tokenResponse + "'");
            latch.countDown();
        };

        String consumerTag = channel.basicConsume(QUEUE_NAME, true, deliverCallback, secondConsumerTag -> {
        });

        latch.await();

        if (tokenResponseRef.get().equals("true")) {
            channel.basicCancel(consumerTag);
            return true;
        } else if (tokenResponseRef.get().equals("false")) {
            channel.basicCancel(consumerTag);
            return false;
        } else {
            channel.basicCancel(consumerTag);
            return false;
        }

    }
}
