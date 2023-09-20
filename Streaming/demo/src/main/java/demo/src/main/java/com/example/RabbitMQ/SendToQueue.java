package demo.src.main.java.com.example.RabbitMQ;

import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
public class SendToQueue {

    private static final Logger logger = LogManager.getLogger(SendToQueue.class);

    @Autowired
    private ReceiveFromQueue receive;
    private final static String QUEUE_NAME = "validateTokenToBackend";

    public boolean sendToken(String token) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.basicPublish("", QUEUE_NAME, null, token.getBytes(StandardCharsets.UTF_8));
            // System.out.println(" [x] Sent '" + token + "'");
            logger.info(" [x] Sent '" + token + "'");
        }
        return receive.receiveTokenResponse();
    }
}