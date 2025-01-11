package com.volod.streaming.domain.exceptions;

import java.util.UUID;

public class VideoNotFoundException extends Exception {

    public VideoNotFoundException(String message) {
        super(message);
    }

    public static VideoNotFoundException of(UUID uuid) {
        return new VideoNotFoundException("Video not found: " + uuid);
    }
}
