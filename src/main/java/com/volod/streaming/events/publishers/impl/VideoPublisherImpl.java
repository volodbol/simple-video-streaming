package com.volod.streaming.events.publishers.impl;

import com.volod.streaming.domain.events.EventVideoEngagement;
import com.volod.streaming.events.publishers.VideoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoPublisherImpl implements VideoPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishVideoEngagement(EventVideoEngagement event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
