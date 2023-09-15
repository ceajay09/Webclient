package demo.src.main.java.com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class StreamingService {

    // private static final String FORMAT = "classpath:videos/%s.mp4";
    private static final String PATH = "demo/src/main/resources/videos/";

    @Autowired
    private ResourceLoader resourceLoader;

    public Mono<Resource> getVideo(String title) {
        listFilesInDirectory(PATH);
        // return Mono.fromSupplier(() ->
        // resourceLoader.getResource(String.format(FORMAT, title)));
        try {
            Resource resource = resourceLoader.getResource("file:" + PATH + title);
            if (resource.exists()) {
                return Mono.just(resource);
            } else {
                throw new FileNotFoundException("Resource not found: " + resource.getFilename());
            }
        } catch (IOException e) {
            return Mono.error(e);
        }
        // return Mono.fromSupplier(() -> resourceLoader
        // .getResource("file:" + PATH + title));
    }

    private void listFilesInDirectory(String directoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
