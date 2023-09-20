package demo.src.main.java.com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import demo.src.main.java.com.example.repository.VideoRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class StreamingService {

    // private static final String FORMAT = "classpath:videos/%s.mp4";
    private static final String PATH = "demo/src/main/resources/videos/";
    private static final int CHUNK_SIZE = 1024;

    @Autowired
    private ResourceLoader resourceLoader;

    public ResponseEntity<byte[]> prepareContent(String title, String fileType, String range) {
        try {
            Resource resource = resourceLoader.getResource("file:" + PATH + title + "." +
                    fileType);
            if (resource.exists()) {
                File file = resource.getFile();
                long fileSize = file.length();

                // Range-Header-Parsing und Bestimmung der Bytebereiche
                String[] ranges = range.replace("bytes=", "").split("-");
                long startRange = Long.parseLong(ranges[0]);
                long endRange = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileSize - 1;

                // Überprüfen und Anpassen des Endbereichs
                endRange = Math.min(endRange, fileSize - 1);

                // Dateiinhalt lesen
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                byte[] content = new byte[(int) (endRange - startRange + 1)];
                randomAccessFile.seek(startRange);
                randomAccessFile.readFully(content);
                randomAccessFile.close();

                // // Erstellen der ResponseEntity mit den benötigten Headern
                HttpHeaders headers = new HttpHeaders();
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header("Content-Length", String.valueOf(content.length))
                        .header("Content-Type", "video/" + "mp4")
                        .header("Expires", "0")
                        .header("Cache-Control", "no-cache, no-store")
                        .header("Connection", "keep-alive")
                        .header("Accept-Ranges", "bytes")
                        .header("Content-Transfer-Encoding", "binary")
                        .header("Content-Range", "bytes " + startRange + "-" + endRange + "/" + fileSize)
                        .headers(headers)
                        .body(content);
            } else {
                throw new FileNotFoundException("Resource not found: " +
                        resource.getFilename());
            }
        } catch (IOException e) {
            // Hier könnten Sie einen geeigneten Fehlerstatus zurückgeben
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // public Mono<ResponseEntity<Flux<DataBuffer>>> getVideo(String title, String
    // range) {
    // Resource resource = resourceLoader.getResource("file:" + PATH + title);

    // if (resource.exists()) {
    // Flux<DataBuffer> dataBuffer = DataBufferUtils.read(resource, new
    // DefaultDataBufferFactory(), CHUNK_SIZE);
    // return Mono.just(
    // ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
    // .contentType(MediaType.valueOf("video/mp4"))
    // .header(HttpHeaders.CONTENT_RANGE, range)
    // .body(dataBuffer));
    // } else {
    // // Rückgabe einer ResponseEntity mit einem Fehlerstatus
    // return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    // }

    // public Mono<ResponseEntity<Flux<DataBuffer>>> getVideo(String title, String
    // fileType, String range) {
    // Video video = this.videoRepository.findById(id).get();

    // java.io.File file = new java.io.File(video.getFile().getUrl());
    // InputStream fis = new FileInputStream(file);

    // try {
    // Resource resource = resourceLoader.getResource("file:" + PATH + title + "." +
    // fileType);
    // if (resource.exists()) {
    // File file = resource.getFile();
    // long fileSize = file.length();

    // // Range-Header-Parsing und Bestimmung der Bytebereiche
    // String[] ranges = range.replace("bytes=", "").split("-");
    // long startRange = Long.parseLong(ranges[0]);
    // long endRange = ranges.length > 1 && !ranges[1].isEmpty() ?
    // Long.parseLong(ranges[1]) : fileSize - 1;

    // // Überprüfen und Anpassen des Endbereichs
    // endRange = Math.min(endRange, fileSize - 1);

    // // Dateiinhalt lesen
    // RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
    // byte[] content = new byte[(int) (endRange - startRange + 1)];
    // randomAccessFile.seek(startRange);
    // // randomAccessFile.readFully(content);

    // // // Erstellen der ResponseEntity mit den benötigten Headern
    // // HttpHeaders headers = new HttpHeaders();
    // // headers.add("Content-Range", "bytes " + startRange + "-" + endRange + "/"
    // +
    // // fileSize);
    // // headers.add("Accept-Ranges", "bytes");
    // // return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
    // // .headers(headers)
    // // .body(content);
    // // } else {
    // // throw new FileNotFoundException("Resource not found: " +
    // // resource.getFilename());
    // // }
    // // } catch (IOException e) {
    // // // Hier könnten Sie einen geeigneten Fehlerstatus zurückgeben
    // // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // // }
    // // }

    // // Erstellen eines InputStreams aus dem RandomAccessFile
    // InputStream inputStream =
    // Channels.newInputStream(randomAccessFile.getChannel());
    // // Erstellen eines DataBuffer-Flux aus dem InputStream
    // Flux<DataBuffer> dataBufferFlux = DataBufferUtils.readInputStream(() ->
    // inputStream,
    // new DefaultDataBufferFactory(), CHUNK_SIZE);

    // // Erstellen der ResponseEntity mit den benötigten Headern
    // HttpHeaders headers = new HttpHeaders();
    // // headers.add("Accept-Ranges", "bytes");
    // //
    // headers.setContentType(MediaType.valueOf(resource.getFile().getContentType()));

    // return Mono.just(ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
    // .header("Content-Type", "video/"+ "mp4")
    // .header("Expires", "0")
    // .header("Cache-Control", "no-cache, no-store")
    // .header("Connection", "keep-alive")
    // .header("Accept-Ranges", "bytes")
    // .header("Content-Transfer-Encoding", "binary")
    // .header("Content-Range", "bytes " + startRange + "-" + endRange + "/" +
    // fileSize)
    // .body(dataBufferFlux));
    // } else {
    // throw new FileNotFoundException("Resource not found: " +
    // resource.getFilename());
    // }
    // } catch (IOException e) {
    // return Mono.error(e);
    // }
    // }

    private void listFilesInDirectory(String directoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
