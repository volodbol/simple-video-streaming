package com.volod.streaming.events.subscribers;

import com.volod.streaming.domain.events.EventVideoLoad;
import org.springframework.context.event.EventListener;

public interface VideoSubscriber {
    @EventListener
    void onVideoLoad(EventVideoLoad event);
}
