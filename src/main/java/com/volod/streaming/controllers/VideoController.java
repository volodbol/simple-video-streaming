package com.volod.streaming.controllers;

import com.volod.streaming.dto.responses.ResponseVideo;
import com.volod.streaming.exceptions.VideoNotFoundException;
import com.volod.streaming.services.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @Operation(summary = "Get listed videos (last updated first)")
    @GetMapping
    public Slice<ResponseVideo> getVideos(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return this.videoService.getVideos(page);
    }

    @Operation(summary = "Post a video")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseVideo postVideo(@RequestParam("file") MultipartFile file) {
        return this.videoService.postVideo(file);
    }

    @Operation(summary = "Hide a video")
    @DeleteMapping("/{id}")
    public ResponseVideo hideVideo(@PathVariable UUID id) throws VideoNotFoundException {
        return this.videoService.hideVideo(id);
    }
}
