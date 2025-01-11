package com.volod.streaming.events.subscribers;

import com.volod.streaming.domain.events.EventVideoEngagement;
import org.springframework.context.event.EventListener;

public interface VideoSubscriber {
    @EventListener
    void onVideoEngagement(EventVideoEngagement event);
}
