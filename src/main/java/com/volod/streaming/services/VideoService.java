package com.volod.streaming.services;

import com.volod.streaming.dto.responses.ResponseVideo;
import com.volod.streaming.exceptions.VideoNotFoundException;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface VideoService {
    Slice<ResponseVideo> getVideos(Integer page);
    ResponseVideo hideVideo(UUID id) throws VideoNotFoundException;
}
