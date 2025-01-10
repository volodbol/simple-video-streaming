package com.volod.streaming.services;

import com.volod.streaming.dto.responses.ResponseVideo;
import org.springframework.data.domain.Slice;

public interface VideoService {
    Slice<ResponseVideo> getVideos(Integer page);
}
