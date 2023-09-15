package demo.src.main.java.com.example;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import demo.src.main.java.com.example.RabbitMQ.SendToQueue;

@SpringBootApplication
@RestController
public class SpringwebfluxVideoStreamingApplication {

    private static final Logger logger = LogManager.getLogger(SpringwebfluxVideoStreamingApplication.class);

    @Autowired
    private StreamingService service;

    @Autowired
    private SendToQueue send;

    @GetMapping(value = "/api/video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideos(@PathVariable String title, @RequestHeader("Range") String range,
            HttpServletRequest request) throws IOException, TimeoutException, InterruptedException {
        // Token aus dem Authorization-Header der Anfrage erhalten
        String token = request.getHeader("Authorization");
        // Token validieren (z.B. mit einer JWT-Bibliothek)
        if (send.sendToken(token)) {
            System.out.println("range in bytes() : " + range);
            return service.getVideo(title);
        } else {
            logger.warn("getAccountFromToken: Token invalid:" + token);
        }
        return service.getVideo(title);
    }

    // @GetMapping(value = "/api/video/{title}", produces = "video/mp4")
    // public ResponseEntity<Map<String, String>> getVideoData() {
    // Map<String, String> videoData = new HashMap<>();
    // videoData.put("videoUrl",
    // "http://streamingProjektServer:Port/video/titelDesVideos");

    // return ResponseEntity.ok(videoData);
    // }

    public static void main(String[] args) {
        // SpringApplication.run(SpringwebfluxVideoStreamingApplication.class, args);
        SpringApplication app = new SpringApplication(SpringwebfluxVideoStreamingApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
    }
}
