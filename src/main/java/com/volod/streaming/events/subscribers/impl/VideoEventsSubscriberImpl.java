package com.volod.streaming.events.subscribers.impl;

import com.volod.streaming.domain.events.EventVideoEngagement;
import com.volod.streaming.events.subscribers.VideoEventsSubscriber;
import com.volod.streaming.services.VideoEngagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoEventsSubscriberImpl implements VideoEventsSubscriber {

    // Services
    private final VideoEngagementService videoEngagementService;

    @Override
    public void onVideoEngagement(EventVideoEngagement event) {
        try {
            this.videoEngagementService.updateEngagements(event);
        } catch (RuntimeException ex) {
            log.error("Can't update video engagement", ex);
        }
    }

}
