package demo.src.main.java.com.example;

import demo.src.main.java.com.example.RabbitMQ.SendToQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeoutException;



@RestController
//@CrossOrigin(origins = "http://localhost:8081")
public class Controller {

    private static final Logger logger = LogManager.getLogger(SpringwebfluxVideoStreamingApplication.class);

    @Autowired
    private StreamingService service;

    @Autowired
    private SendToQueue send;

    @GetMapping(value = "/api/video/{title}")
    public void getVideo(@PathVariable String title,
                                  @RequestHeader("Range") String range,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException, TimeoutException, InterruptedException {
        // Token aus dem Authorization-Header der Anfrage erhalten
        String token = request.getHeader("Authorization");

        // Token validieren (z.B. mit einer JWT-Bibliothek)

        if (send.validateToken(token)) {
            System.out.println("range in bytes() : " + range);
            StreamingService.ServiceResponse serviceResponse = service.getVideoFragment(title, "mp4", range);
            logger.info("fileSize " + serviceResponse.getFileSize());

            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", "bytes " + serviceResponse.getStart() + "-" + serviceResponse.getEnd() + "/" + serviceResponse.getFileSize());
            response.setHeader("Content-Length", String.valueOf(serviceResponse.getContent().length));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//            return Mono.just(serviceResponse.getContent());
            response.getOutputStream().write(serviceResponse.getContent());


        } else {
            logger.warn("getAccountFromToken: Token invalid:" + token);
            throw new RuntimeException("Token wrong");
        }
    }

}
