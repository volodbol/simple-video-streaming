package com.volod.streaming.services;

import com.volod.streaming.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.dto.responses.ResponseVideo;
import com.volod.streaming.exceptions.VideoNotFoundException;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoService {
    Slice<ResponseVideo> getVideos(Integer page);
    ResponseVideo postVideo(MultipartFile file);
    ResponseVideo editMetadata(UUID id, RequestVideoMetadataEdit request) throws VideoNotFoundException;
    ResponseVideo hideVideo(UUID id) throws VideoNotFoundException;
}
