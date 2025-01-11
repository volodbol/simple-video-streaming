package com.volod.streaming.events.publishers.impl;

import com.volod.streaming.domain.events.EventVideoEngagement;
import com.volod.streaming.events.publishers.VideoEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoEventsPublisherImpl implements VideoEventsPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishVideoEngagement(EventVideoEngagement event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
