package com.volod.streaming.services;

import com.volod.streaming.domain.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.domain.dto.requests.RequestVideos;
import com.volod.streaming.domain.dto.responses.ResponseVideo;
import com.volod.streaming.domain.dto.responses.ResponseVideoLoad;
import com.volod.streaming.domain.dto.responses.ResponseVideoPlay;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoService {
    Slice<ResponseVideo> getVideos(Integer page);
    Slice<ResponseVideo> findVideos(RequestVideos request);
    ResponseVideoLoad loadVideo(UUID id) throws VideoNotFoundException;
    ResponseVideoPlay playVideo(UUID id) throws VideoNotFoundException;
    @Transactional
    ResponseVideo postVideo(MultipartFile file);
    ResponseVideo editMetadata(UUID id, RequestVideoMetadataEdit request) throws VideoNotFoundException;
    ResponseVideo hideVideo(UUID id) throws VideoNotFoundException;
}
