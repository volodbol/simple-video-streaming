package com.volod.streaming.domain.dto.responses;

import com.volod.streaming.domain.model.VideoEngagement;
import com.volod.streaming.domain.model.VideoEngagementType;

import java.util.Map;
import java.util.UUID;

public record ResponseVideoEngagement(
        UUID id,
        Map<VideoEngagementType, Integer> engagements
) {
    public static ResponseVideoEngagement of(VideoEngagement videoEngagement) {
        return new ResponseVideoEngagement(
                videoEngagement.getVideoId(),
                videoEngagement.getEngagements()
        );
    }
}
