package com.volod.streaming.services.impl;

import com.volod.streaming.domain.dto.responses.ResponseVideoEngagement;
import com.volod.streaming.domain.events.EventVideoEngagement;
import com.volod.streaming.domain.exceptions.VideoEngagementNotFoundException;
import com.volod.streaming.domain.model.Video;
import com.volod.streaming.domain.model.VideoEngagement;
import com.volod.streaming.repositories.VideoEngagementRepository;
import com.volod.streaming.services.VideoEngagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoEngagementServiceImpl implements VideoEngagementService {

    // Repositories
    private final VideoEngagementRepository videoEngagementRepository;

    @Override
    public ResponseVideoEngagement getVideoEngagement(UUID videoId) throws VideoEngagementNotFoundException {
        return this.videoEngagementRepository.findByVideoId(videoId)
                .map(ResponseVideoEngagement::of)
                .orElseThrow(() -> VideoEngagementNotFoundException.of(videoId));
    }

    @Override
    public void createVideoEngagement(Video video) {
        this.videoEngagementRepository.save(new VideoEngagement(video));
    }

    @Override
    public void updateEngagements(EventVideoEngagement event) {
        var videoId = event.videoId();
        switch (event.engagementType()) {
            case IMPRESSION -> this.videoEngagementRepository.updateImpression(videoId);
            case VIEW -> this.videoEngagementRepository.updateViews(videoId);
        }
    }
}
