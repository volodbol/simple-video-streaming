package com.volod.streaming.controllers;

import com.volod.streaming.domain.dto.responses.ResponseVideoEngagement;
import com.volod.streaming.domain.exceptions.VideoEngagementNotFoundException;
import com.volod.streaming.services.VideoEngagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Videos Engagements API")
@RestController
@RequestMapping("/v1/videos/{videoId}/engagements")
@RequiredArgsConstructor
public class VideoEngagementController {

    // Services
    private final VideoEngagementService videoEngagementService;

    @Operation(summary = "Get video engagements")
    @GetMapping
    public ResponseVideoEngagement getVideoEngagement(@PathVariable UUID videoId) throws VideoEngagementNotFoundException {
        return this.videoEngagementService.getVideoEngagement(videoId);
    }

}
