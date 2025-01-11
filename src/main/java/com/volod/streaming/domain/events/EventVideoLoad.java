package com.volod.streaming.domain.events;

import com.volod.streaming.domain.model.Video;

import java.util.UUID;

public record EventVideoLoad(
        UUID id
) {
    public static EventVideoLoad of(Video video) {
        return new EventVideoLoad(
                video.getId()
        );
    }
}
