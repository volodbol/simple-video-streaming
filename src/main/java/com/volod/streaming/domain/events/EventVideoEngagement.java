package com.volod.streaming.domain.events;

import com.volod.streaming.domain.model.Video;
import com.volod.streaming.domain.model.VideoEngagementType;

import java.util.UUID;

public record EventVideoEngagement(
        UUID videoId,
        VideoEngagementType engagementType
) {
    public static EventVideoEngagement of(Video video, VideoEngagementType engagementType) {
        return new EventVideoEngagement(
                video.getId(),
                engagementType
        );
    }
}
