package com.volod.streaming.controllers;

import com.volod.streaming.dto.responses.ResponseVideo;
import com.volod.streaming.exceptions.VideoNotFoundException;
import com.volod.streaming.services.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @Operation(summary = "Get listed videos")
    @GetMapping
    public Slice<ResponseVideo> getVideos(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return this.videoService.getVideos(page);
    }

    @Operation(summary = "Hide a video")
    @PostMapping("/{id}")
    public ResponseVideo hideVideo(@PathVariable UUID id) throws VideoNotFoundException {
        return this.videoService.hideVideo(id);
    }
}
