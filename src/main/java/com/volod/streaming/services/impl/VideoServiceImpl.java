package com.volod.streaming.services.impl;

import com.volod.streaming.domain.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.domain.dto.requests.RequestVideos;
import com.volod.streaming.domain.dto.responses.ResponseVideo;
import com.volod.streaming.domain.dto.responses.ResponseVideoLoad;
import com.volod.streaming.domain.dto.responses.ResponseVideoPlay;
import com.volod.streaming.domain.events.EventVideoEngagement;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import com.volod.streaming.domain.model.AbstractAuditPersistable_;
import com.volod.streaming.domain.model.Video;
import com.volod.streaming.domain.model.VideoEngagementType;
import com.volod.streaming.events.publishers.VideoEventsPublisher;
import com.volod.streaming.repositories.VideoRepository;
import com.volod.streaming.services.VideoEngagementService;
import com.volod.streaming.services.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    // Services
    private final VideoEngagementService videoEngagementService;
    // Repositories
    private final VideoRepository videoRepository;
    // Publishes
    private final VideoEventsPublisher videoEventsPublisher;

    @Override
    public Slice<ResponseVideo> getVideos(Integer page, Integer size) {
        return this.videoRepository.findAllByHiddenIsFalse(
                PageRequest.of(page, size, Sort.by(DESC, AbstractAuditPersistable_.UPDATED_AT))
        ).map(ResponseVideo::of);
    }

    @Override
    public Slice<ResponseVideo> findVideos(RequestVideos request) {
        var pageable = request.toPageable();
        var specification = request.toSpecification();
        return this.videoRepository.findAll(specification, pageable).map(ResponseVideo::of);
    }

    @Override
    public ResponseVideoLoad loadVideo(UUID id) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        this.videoEventsPublisher.publishVideoEngagement(EventVideoEngagement.of(video, VideoEngagementType.IMPRESSION));
        return ResponseVideoLoad.of(video);
    }

    @Override
    public ResponseVideoPlay playVideo(UUID id) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        this.videoEventsPublisher.publishVideoEngagement(EventVideoEngagement.of(video, VideoEngagementType.VIEW));
        return ResponseVideoPlay.of(video);
    }

    @Override
    public ResponseVideo postVideo(MultipartFile file) {
        log.debug("Post video within transaction: {}", TransactionSynchronizationManager.isActualTransactionActive());
        var video = Video.random(false);
        this.videoRepository.save(video);
        this.videoEngagementService.createVideoEngagement(video);
        return ResponseVideo.of(video);
    }

    @Override
    public ResponseVideo editMetadata(UUID id, RequestVideoMetadataEdit request) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        video.update(request);
        this.videoRepository.saveAndFlush(video);
        return ResponseVideo.of(video);
    }

    @Override
    public ResponseVideo hideVideo(UUID id) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        video.setHidden(true);
        this.videoRepository.saveAndFlush(video);
        return ResponseVideo.of(video);
    }
}
