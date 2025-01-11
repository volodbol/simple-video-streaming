package com.volod.streaming.events.subscribers.impl;

import com.volod.streaming.domain.events.EventVideoLoad;
import com.volod.streaming.events.subscribers.VideoSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoSubscriberImpl implements VideoSubscriber {

    @Override
    public void onVideoLoad(EventVideoLoad event) {
        // no actions
    }

}
