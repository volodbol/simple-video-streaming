package com.volod.streaming.controllers;

import com.volod.streaming.domain.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.domain.dto.requests.RequestVideos;
import com.volod.streaming.domain.dto.responses.ResponseVideo;
import com.volod.streaming.domain.dto.responses.ResponseVideoLoad;
import com.volod.streaming.domain.dto.responses.ResponseVideoPlay;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import com.volod.streaming.services.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Videos API")
@RestController
@RequestMapping("v1/videos")
@RequiredArgsConstructor
public class VideoController {

    // Services
    private final VideoService videoService;

    @Operation(summary = "Get listed videos (last updated first)")
    @GetMapping
    public Slice<ResponseVideo> getVideos(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "50") @Max(100) Integer size
    ) {
        return this.videoService.getVideos(page, size);
    }

    @Operation(summary = "Find listed videos")
    @PostMapping("/search")
    public Slice<ResponseVideo> findVideos(@RequestBody RequestVideos request) {
        return this.videoService.findVideos(request);
    }

    @Operation(summary = "Load a video")
    @GetMapping("/{id}")
    public ResponseVideoLoad loadVideo(@PathVariable UUID id) throws VideoNotFoundException {
        return this.videoService.loadVideo(id);
    }

    @Operation(summary = "Play a video")
    @GetMapping("/{id}/play")
    public ResponseVideoPlay playVideo(@PathVariable UUID id) throws VideoNotFoundException {
        return this.videoService.playVideo(id);
    }

    @Operation(summary = "Post a video")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseVideo postVideo(@RequestParam("file") MultipartFile file) {
        return this.videoService.postVideo(file);
    }

    @Operation(summary = "Edit a video metadata")
    @PutMapping("/{id}")
    public ResponseVideo editMetadata(
            @PathVariable("id") UUID id,
            @RequestBody @Valid RequestVideoMetadataEdit request
    ) throws VideoNotFoundException {
        return this.videoService.editMetadata(id, request);
    }

    @Operation(summary = "Hide a video")
    @DeleteMapping("/{id}")
    public ResponseVideo hideVideo(@PathVariable UUID id) throws VideoNotFoundException {
        return this.videoService.hideVideo(id);
    }
}
