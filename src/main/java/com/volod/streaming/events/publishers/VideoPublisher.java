package com.volod.streaming.events.publishers;

import com.volod.streaming.domain.events.EventVideoEngagement;

public interface VideoPublisher {
    void publishVideoEngagement(EventVideoEngagement event);
}
