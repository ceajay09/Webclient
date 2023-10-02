package demo.src.main.java.com.example;

import demo.src.main.java.com.example.RabbitMQ.SendToQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@org.springframework.stereotype.Controller
public class Controller {

    private static final Logger logger = LogManager.getLogger(SpringwebfluxVideoStreamingApplication.class);

    @Autowired
    private StreamingService service;

    @Autowired
    private SendToQueue send;

    @GetMapping(value = "/api/video/{title}")
    public Mono<byte[]> getVideos(@PathVariable String title,
                                  @RequestHeader("Range") String range,
                                  HttpServletRequest request) throws IOException, TimeoutException, InterruptedException {
        // Token aus dem Authorization-Header der Anfrage erhalten
        String token = request.getHeader("Authorization");

        // Token validieren (z.B. mit einer JWT-Bibliothek)

        if (send.sendToken(token)) {
            System.out.println("range in bytes() : " + range);
            return Mono.just(service.prepareContent(title, "mp4", range).getContent());
        } else {
            logger.warn("getAccountFromToken: Token invalid:" + token);
            throw new RuntimeException("Token wrong");
        }
    }

}
