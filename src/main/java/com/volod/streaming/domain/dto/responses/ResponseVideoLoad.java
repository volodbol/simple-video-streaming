package com.volod.streaming.domain.dto.responses;

import com.volod.streaming.domain.model.Video;

import java.util.UUID;

public record ResponseVideoLoad(
        UUID id,
        String link,
        String title,
        String director,
        String mainActor,
        String genre,
        long duration
) {

    public static ResponseVideoLoad of(Video video) {
        return new ResponseVideoLoad(
                video.getId(),
                video.getBucketLink(),
                video.getTitle(),
                video.getDirector(),
                video.getMainActor(),
                video.getGenre(),
                video.getDuration()
        );
    }

}
