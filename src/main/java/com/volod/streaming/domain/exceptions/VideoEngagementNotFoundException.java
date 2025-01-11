package com.volod.streaming.domain.exceptions;

import java.util.UUID;

public class VideoEngagementNotFoundException extends Exception {

    public VideoEngagementNotFoundException(String message) {
        super(message);
    }

    public static VideoEngagementNotFoundException of(UUID uuid) {
        return new VideoEngagementNotFoundException("Video not found: " + uuid);
    }
}
