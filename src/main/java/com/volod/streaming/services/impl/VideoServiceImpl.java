package com.volod.streaming.services.impl;

import com.volod.streaming.domain.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.domain.dto.responses.ResponseVideo;
import com.volod.streaming.domain.dto.responses.ResponseVideoLoad;
import com.volod.streaming.domain.events.EventVideoLoad;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import com.volod.streaming.events.publishers.VideoPublisher;
import com.volod.streaming.model.AbstractAuditPersistable_;
import com.volod.streaming.domain.model.Video;
import com.volod.streaming.repositories.VideoRepository;
import com.volod.streaming.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    // Repositories
    private final VideoRepository videoRepository;
    // Publishes
    private final VideoPublisher videoPublisher;

    @Override
    public Slice<ResponseVideo> getVideos(Integer page) {
        return this.videoRepository.findAllByHiddenIsFalse(
                PageRequest.of(page, 50, Sort.by(DESC, AbstractAuditPersistable_.UPDATED_AT))
        ).map(ResponseVideo::of);
    }

    @Override
    public ResponseVideoLoad loadVideo(UUID id) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        this.videoPublisher.publishVideoLoad(EventVideoLoad.of(video));
        return ResponseVideoLoad.of(video);
    }

    @Override
    public ResponseVideo postVideo(MultipartFile file) {
        var video = Video.random(false);
        this.videoRepository.save(video);
        return ResponseVideo.of(video);
    }

    @Override
    public ResponseVideo editMetadata(UUID id, RequestVideoMetadataEdit request) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        video.update(request);
        this.videoRepository.save(video);
        return ResponseVideo.of(video);
    }

    @Override
    public ResponseVideo hideVideo(UUID id) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        video.setHidden(true);
        this.videoRepository.save(video);
        return ResponseVideo.of(video);
    }
}
