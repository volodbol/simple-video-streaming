package com.volod.streaming.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volod.streaming.domain.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.domain.dto.requests.RequestVideos;
import com.volod.streaming.domain.dto.responses.ResponseException;
import com.volod.streaming.domain.dto.responses.ResponseVideo;
import com.volod.streaming.domain.dto.responses.ResponseVideoLoad;
import com.volod.streaming.domain.dto.responses.ResponseVideoPlay;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import com.volod.streaming.domain.model.AbstractAuditPersistable_;
import com.volod.streaming.domain.model.Video;
import com.volod.streaming.services.VideoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VideoController.class)
class VideoControllerTest {

    @SuppressWarnings("unused")
    @MockitoBean
    private VideoService videoService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void beforeEach() {
        reset(
                this.videoService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.videoService
        );
    }

    @Test
    void runtimeExceptionOccurredTest() throws Exception {
        // Arrange
        when(this.videoService.getVideos(any(), any())).thenThrow(new NullPointerException());
        var expected = ResponseException.of("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

        // Act + Assert
        this.mvc.perform(get("/v1/videos"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
        verify(this.videoService).getVideos(any(), any());
    }

    @Test
    void malformedRequestBodyTest() throws Exception {
        // Arrange
        var expected = ResponseException.of("Malformed request body", HttpStatus.BAD_REQUEST);

        // Act + Assert
        //noinspection JsonStandardCompliance
        this.mvc.perform(
                        post("/v1/videos/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{foo}")
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
    }

    @Test
    void invalidRequestParamTest() throws Exception {
        // Arrange
        var expected = ResponseException.of("size: must be less than or equal to 100", HttpStatus.BAD_REQUEST);

        // Act + Assert
        this.mvc.perform(get("/v1/videos?size=150"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
    }

    @Test
    void invalidRequestBodyTest() throws Exception {
        // Arrange
        @SuppressWarnings("DataFlowIssue")
        var request = new RequestVideoMetadataEdit(
                null,
                "Amazing director",
                "",
                "Nice genre",
                0
        );
        var expected = ResponseException.of(
                "duration: must be greater than 0, mainActor: must not be empty, title: must not be empty",
                HttpStatus.BAD_REQUEST
        );

        // Act + Assert
        this.mvc.perform(
                        put("/v1/videos/eff16ccc-b888-49ee-a08a-59dc042cc977")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
    }

    @Test
    void getVideosTest() throws Exception {
        // Arrange
        var content = IntStream.range(0, 10).mapToObj(i -> Video.random(false)).map(ResponseVideo::of).toList();
        var expected = new SliceImpl<>(
                content,
                PageRequest.of(0, 50, Sort.by(DESC, AbstractAuditPersistable_.UPDATED_AT)),
                false
        );
        when(this.videoService.getVideos(any(), any())).thenReturn(expected);

        // Act + Assert
        this.mvc.perform(get("/v1/videos"))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
        verify(this.videoService).getVideos(any(), any());
    }

    @Test
    void findVideosTest() throws Exception {
        // Arrange
        var content = IntStream.range(0, 10).mapToObj(i -> Video.random(false)).map(ResponseVideo::of).toList();
        var expected = new SliceImpl<>(
                content,
                PageRequest.of(0, 50, Sort.by(DESC, AbstractAuditPersistable_.UPDATED_AT)),
                false
        );
        var request = RequestVideos.hardcoded();
        when(this.videoService.findVideos(request)).thenReturn(expected);

        // Act + Assert
        this.mvc.perform(
                        post("/v1/videos/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(request))
                ).andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
        verify(this.videoService).findVideos(request);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void loadVideoTest(boolean exists) throws Exception {
        // Arrange
        var uuid = UUID.fromString("eff16ccc-b888-49ee-a08a-59dc042cc977");
        var exception = VideoNotFoundException.of(uuid);
        var response = ResponseVideoLoad.of(Video.random(false));
        var responseException = ResponseException.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        if (exists) {
            when(this.videoService.loadVideo(uuid)).thenReturn(response);
        } else {
            when(this.videoService.loadVideo(uuid)).thenThrow(exception);
        }

        // Act + Assert
        this.mvc.perform(get("/v1/videos/{id}", uuid))
                .andExpect(exists ? status().isOk() : status().isNotFound())
                .andExpect(
                        exists ? content().json(this.objectMapper.writeValueAsString(response)) :
                                content().json(this.objectMapper.writeValueAsString(responseException))
                );
        verify(this.videoService).loadVideo(uuid);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void playVideoTest(boolean exists) throws Exception {
        // Arrange
        var uuid = UUID.fromString("eff16ccc-b888-49ee-a08a-59dc042cc977");
        var exception = VideoNotFoundException.of(uuid);
        var response = ResponseVideoPlay.of(Video.random(false));
        var responseException = ResponseException.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        if (exists) {
            when(this.videoService.playVideo(uuid)).thenReturn(response);
        } else {
            when(this.videoService.playVideo(uuid)).thenThrow(exception);
        }

        // Act + Assert
        this.mvc.perform(get("/v1/videos/{id}/play", uuid))
                .andExpect(exists ? status().isOk() : status().isNotFound())
                .andExpect(
                        exists ? content().json(this.objectMapper.writeValueAsString(response)) :
                                content().json(this.objectMapper.writeValueAsString(responseException))
                );
        verify(this.videoService).playVideo(uuid);
    }

    @Test
    void postVideoTest() throws Exception {
        // Arrange
        var videoFile = new MockMultipartFile("file", "video.txt", "text/plain", "video.txt".getBytes());
        var expected = ResponseVideo.of(Video.random(false));
        when(this.videoService.postVideo(any())).thenReturn(expected);

        // Act
        this.mvc.perform(multipart("/v1/videos").file(videoFile))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expected)));
        verify(this.videoService).postVideo(videoFile);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void editMetadataTest(boolean exists) throws Exception {
        // Arrange
        var uuid = UUID.fromString("eff16ccc-b888-49ee-a08a-59dc042cc977");
        var exception = VideoNotFoundException.of(uuid);
        var request = RequestVideoMetadataEdit.hardcoded();
        var response = ResponseVideo.of(Video.random(false));
        var responseException = ResponseException.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        if (exists) {
            when(this.videoService.editMetadata(uuid, request)).thenReturn(response);
        } else {
            when(this.videoService.editMetadata(uuid, request)).thenThrow(exception);
        }

        // Act + Assert
        this.mvc.perform(
                        put("/v1/videos/{id}", uuid)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(request))
                ).andExpect(exists ? status().isOk() : status().isNotFound())
                .andExpect(
                        exists ? content().json(this.objectMapper.writeValueAsString(response)) :
                                content().json(this.objectMapper.writeValueAsString(responseException))
                );
        verify(this.videoService).editMetadata(uuid, request);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void hideVideoTest(boolean exists) throws Exception {
        // Arrange
        var uuid = UUID.fromString("eff16ccc-b888-49ee-a08a-59dc042cc977");
        var exception = VideoNotFoundException.of(uuid);
        var response = ResponseVideo.of(Video.random(false));
        var responseException = ResponseException.of(exception.getMessage(), HttpStatus.NOT_FOUND);
        if (exists) {
            when(this.videoService.hideVideo(uuid)).thenReturn(response);
        } else {
            when(this.videoService.hideVideo(uuid)).thenThrow(exception);
        }

        // Act + Assert
        this.mvc.perform(delete("/v1/videos/{id}", uuid))
                .andExpect(exists ? status().isOk() : status().isNotFound())
                .andExpect(
                        exists ? content().json(this.objectMapper.writeValueAsString(response)) :
                                content().json(this.objectMapper.writeValueAsString(responseException))
                );
        verify(this.videoService).hideVideo(uuid);
    }
}