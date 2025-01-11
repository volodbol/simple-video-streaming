package com.volod.streaming.domain.dto.responses;

import com.volod.streaming.domain.model.Video;

import java.util.UUID;

public record ResponseVideoPlay(
        UUID id,
        String link
) {
    public static ResponseVideoPlay of(Video video) {
        return new ResponseVideoPlay(
                video.getId(),
                video.getBucketLink()
        );
    }
}
