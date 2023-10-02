package demo.src.main.java.com.example;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.realm.JAASMemoryLoginModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import demo.src.main.java.com.example.RabbitMQ.SendToQueue;

@CrossOrigin(origins = "http://localhost:8081")
@SpringBootApplication
@RestController
public class SpringwebfluxVideoStreamingApplication {



    // @PostConstruct
    // public void testData() {
    // Video video = new Video();
    // video.setName("Testname");
    // }

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
