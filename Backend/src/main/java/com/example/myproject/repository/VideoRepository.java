package com.example.myproject.repository;

import com.example.myproject.repository.Video;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VideoRepository {
    Mono<Video> getVideoByName(String name);

    Flux<Video> getAllVideos();

    Mono<Video> addVideo(Video video);
}
