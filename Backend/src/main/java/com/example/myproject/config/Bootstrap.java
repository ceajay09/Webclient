package com.example.myproject.config;

import com.example.myproject.repository.Video;
import com.example.myproject.repository.VideoRepository;
import com.example.myproject.services.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {
    private final VideoRepository videoRepository;
    private final IFileService fileService;

    public Bootstrap(VideoRepository videoRepository, IFileService fileService) {
        this.videoRepository = videoRepository;
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) {
        fileService.getAllFiles()
                .doOnNext(path -> log.debug("found file in path: " + path.toUri() + " FileName: " + path.getFileName()))
                .flatMap(path -> {
                    Video video = new Video();
                    video.setName(path.getFileName().toString());
                    video.setLocation(path);
                    return videoRepository.addVideo(video);
                })
                .subscribe();

        videoRepository.getAllVideos()
                .doOnNext(video -> log.info("Registered video: " + video.getName()))
                .subscribe();
    }
}
