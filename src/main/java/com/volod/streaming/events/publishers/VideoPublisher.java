package com.volod.streaming.events.publishers;

import com.volod.streaming.domain.events.EventVideoLoad;

public interface VideoPublisher {
    void publishVideoLoad(EventVideoLoad event);
}
