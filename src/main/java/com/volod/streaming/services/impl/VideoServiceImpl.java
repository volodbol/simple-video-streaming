package com.volod.streaming.services.impl;

import com.volod.streaming.dto.responses.ResponseVideo;
import com.volod.streaming.exceptions.VideoNotFoundException;
import com.volod.streaming.model.AbstractAuditPersistable_;
import com.volod.streaming.repositories.VideoRepository;
import com.volod.streaming.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    // Repositories
    private final VideoRepository videoRepository;

    @Override
    public Slice<ResponseVideo> getVideos(Integer page) {
        return this.videoRepository.findAllByHiddenIsFalse(
                PageRequest.of(page, 50, Sort.by(DESC, AbstractAuditPersistable_.UPDATED_AT))
        ).map(ResponseVideo::of);
    }

    @Override
    public ResponseVideo hideVideo(UUID id) throws VideoNotFoundException {
        var video = this.videoRepository.findById(id).orElseThrow(() -> VideoNotFoundException.of(id));
        video.setHidden(true);
        this.videoRepository.save(video);
        return ResponseVideo.of(video);
    }
}
