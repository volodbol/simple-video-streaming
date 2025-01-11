package com.volod.streaming.services;

import com.volod.streaming.domain.dto.responses.ResponseVideoEngagement;
import com.volod.streaming.domain.exceptions.VideoEngagementNotFoundException;

import java.util.UUID;

public interface VideoEngagementService {
    ResponseVideoEngagement getVideoEngagement(UUID videoId) throws VideoEngagementNotFoundException;
}
