package com.volod.streaming.dto.responses;

import com.volod.streaming.model.Video;

import java.util.UUID;

public record ResponseVideo(
        UUID id,
        String title,
        String director,
        String mainActor,
        String genre,
        long duration
) {

    public static ResponseVideo of(Video video) {
        return new ResponseVideo(
                video.getId(),
                video.getTitle(),
                video.getDirector(),
                video.getMainActor(),
                video.getGenre(),
                video.getDuration()
        );
    }

}
