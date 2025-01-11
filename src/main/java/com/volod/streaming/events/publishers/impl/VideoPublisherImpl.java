package com.volod.streaming.events.publishers.impl;

import com.volod.streaming.domain.events.EventVideoLoad;
import com.volod.streaming.events.publishers.VideoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoPublisherImpl implements VideoPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishVideoLoad(EventVideoLoad event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
