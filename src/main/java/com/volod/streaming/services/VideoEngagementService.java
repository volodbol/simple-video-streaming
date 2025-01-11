package com.volod.streaming.services;

import com.volod.streaming.domain.dto.responses.ResponseVideoEngagement;
import com.volod.streaming.domain.events.EventVideoEngagement;
import com.volod.streaming.domain.exceptions.VideoEngagementNotFoundException;
import com.volod.streaming.domain.model.Video;

import java.util.UUID;

public interface VideoEngagementService {
    ResponseVideoEngagement getVideoEngagement(UUID videoId) throws VideoEngagementNotFoundException;
    void createVideoEngagement(Video video);
    void updateEngagements(EventVideoEngagement event);
}
