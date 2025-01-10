package com.volod.streaming.controllers;

import com.volod.streaming.dto.responses.ResponseVideo;
import com.volod.streaming.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public Slice<ResponseVideo> getVideos(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return this.videoService.getVideos(page);
    }

}
