package com.volod.streaming.services;

import com.volod.streaming.TestcontainersConfiguration;
import com.volod.streaming.domain.dto.requests.RequestVideoMetadataEdit;
import com.volod.streaming.domain.dto.requests.RequestVideos;
import com.volod.streaming.domain.dto.responses.ResponseVideo;
import com.volod.streaming.domain.exceptions.VideoEngagementNotFoundException;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import com.volod.streaming.domain.model.Video;
import com.volod.streaming.domain.model.VideoEngagementType;
import com.volod.streaming.events.publishers.impl.VideoEventsPublisherImpl;
import com.volod.streaming.events.subscribers.impl.VideoEventsSubscriberImpl;
import com.volod.streaming.repositories.VideoRepository;
import com.volod.streaming.services.impl.VideoEngagementServiceImpl;
import com.volod.streaming.services.impl.VideoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@Import({
        TestcontainersConfiguration.class,
        VideoEngagementServiceImpl.class,
        VideoEventsPublisherImpl.class,
        VideoEventsSubscriberImpl.class,
        VideoServiceImpl.class
})
@DataJpaTest
class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoEngagementService videoEngagementService;
    @Autowired
    private VideoService componentUnderTest;

    @Test
    void getVideosTest() {
        // Arrange
        var videos = IntStream.range(0, 10).mapToObj(i -> Video.random(true)).collect(Collectors.toList());
        videos.addAll(IntStream.range(0, 30).mapToObj(i -> Video.random(false)).toList());
        this.videoRepository.saveAll(videos);

        // Act + Assert
        assertThat(this.componentUnderTest.getVideos(0, 10)).hasSize(10)
                .extracting(ResponseVideo::updatedAt).isNotEqualTo(0).isSortedAccordingTo(Comparator.reverseOrder());
        assertThat(this.componentUnderTest.getVideos(1, 10)).hasSize(10)
                .extracting(ResponseVideo::updatedAt).isNotEqualTo(0).isSortedAccordingTo(Comparator.reverseOrder());
        var lastSlice = this.componentUnderTest.getVideos(2, 10);
        assertThat(lastSlice.isLast()).isTrue();
        assertThat(lastSlice).hasSize(10)
                .extracting(ResponseVideo::updatedAt).isNotEqualTo(0).isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    void findVideosTest() {
        // Arrange
        this.videoRepository.saveAll(Video.dummies1());

        // Act + Assert
        assertThat(this.componentUnderTest.findVideos(new RequestVideos(null, null, null, 0, 3))).hasSize(3);
        assertThat(this.componentUnderTest.findVideos(new RequestVideos("Dune", null, null, 0, 3))).hasSize(2);
        assertThat(this.componentUnderTest.findVideos(new RequestVideos("Dune 2", null, null, 0, 3))).hasSize(1);
        assertThat(this.componentUnderTest.findVideos(new RequestVideos(null, "Mart", null, 0, 3))).hasSize(2);
        assertThat(this.componentUnderTest.findVideos(new RequestVideos(null, "Denis", "Timoth", 0, 3))).hasSize(2);
        assertThat(this.componentUnderTest.findVideos(new RequestVideos("Dun", "Denis", "Timoth", 0, 3))).hasSize(2);
        assertThat(this.componentUnderTest.findVideos(new RequestVideos("Heat", null, null, 0, 3))).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void loadVideoTest(boolean exists) throws VideoEngagementNotFoundException {
        // Arrange
        var id = UUID.randomUUID();
        var videoFile = new MockMultipartFile("video", "video.txt", "text/plain", "video.txt".getBytes());
        if (exists) {
            id = this.componentUnderTest.postVideo(videoFile).id();
        }

        // Act + Assert
        var videoId = id;
        if (exists) {
            var engagement = this.videoEngagementService.getVideoEngagement(videoId);
            assertThat(engagement.engagements()).containsEntry(VideoEngagementType.IMPRESSION, 0);
            assertThatNoException().isThrownBy(() -> this.componentUnderTest.loadVideo(videoId));
            engagement = this.videoEngagementService.getVideoEngagement(videoId);
            assertThat(engagement.engagements()).containsEntry(VideoEngagementType.IMPRESSION, 1);
        } else {
            var actual = catchThrowable(() -> this.componentUnderTest.loadVideo(videoId));
            assertThat(actual).isNotNull()
                    .isInstanceOf(VideoNotFoundException.class)
                    .hasMessage(VideoNotFoundException.of(videoId).getMessage());
            var engagementsThrowable = catchThrowable(() -> this.videoEngagementService.getVideoEngagement(videoId));
            assertThat(engagementsThrowable).isNotNull()
                    .isInstanceOf(VideoEngagementNotFoundException.class)
                    .hasMessage(VideoEngagementNotFoundException.of(videoId).getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void playVideoTest(boolean exists) throws VideoEngagementNotFoundException {
        // Arrange
        var id = UUID.randomUUID();
        var videoFile = new MockMultipartFile("video", "video.txt", "text/plain", "video.txt".getBytes());
        if (exists) {
            id = this.componentUnderTest.postVideo(videoFile).id();
        }

        // Act + Assert
        var videoId = id;
        if (exists) {
            var engagement = this.videoEngagementService.getVideoEngagement(videoId);
            assertThat(engagement.engagements()).containsEntry(VideoEngagementType.VIEW, 0);
            assertThatNoException().isThrownBy(() -> this.componentUnderTest.playVideo(videoId));
            engagement = this.videoEngagementService.getVideoEngagement(videoId);
            assertThat(engagement.engagements()).containsEntry(VideoEngagementType.VIEW, 1);
        } else {
            var actual = catchThrowable(() -> this.componentUnderTest.playVideo(videoId));
            assertThat(actual).isNotNull()
                    .isInstanceOf(VideoNotFoundException.class)
                    .hasMessage(VideoNotFoundException.of(videoId).getMessage());
            var engagementsThrowable = catchThrowable(() -> this.videoEngagementService.getVideoEngagement(videoId));
            assertThat(engagementsThrowable).isNotNull()
                    .isInstanceOf(VideoEngagementNotFoundException.class)
                    .hasMessage(VideoEngagementNotFoundException.of(videoId).getMessage());
        }
    }

    @Test
    void postVideoTest() throws VideoEngagementNotFoundException {
        // Arrange
        var videoFile = new MockMultipartFile("video", "video.txt", "text/plain", "video.txt".getBytes());

        // Act
        var actual = this.componentUnderTest.postVideo(videoFile);
        var actualRepo = this.videoRepository.findById(actual.id()).orElseThrow();
        var engagement = this.videoEngagementService.getVideoEngagement(actual.id());

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actualRepo.isHidden()).isFalse();
        assertThat(engagement.engagements()).containsEntry(VideoEngagementType.IMPRESSION, 0);
        assertThat(engagement.engagements()).satisfies(
                map -> map.values().forEach(integer -> assertThat(integer).isZero())
        );
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void editMetadataTest(boolean exists) throws VideoNotFoundException {
        // Arrange
        var id = UUID.randomUUID();
        var videoFile = new MockMultipartFile("video", "video.txt", "text/plain", "video.txt".getBytes());
        Long updatedAt = null;
        if (exists) {
            var video = this.componentUnderTest.postVideo(videoFile);
            id = video.id();
            updatedAt = video.updatedAt();
        }
        var request = RequestVideoMetadataEdit.hardcoded();

        // Act + Assert
        var videoId = id;
        if (exists) {
            var actual = this.componentUnderTest.editMetadata(id, request);
            assertThat(actual.title()).isEqualTo(request.title());
            assertThat(actual.director()).isEqualTo(request.director());
            assertThat(actual.mainActor()).isEqualTo(request.mainActor());
            assertThat(actual.genre()).isEqualTo(request.genre());
            assertThat(actual.duration()).isEqualTo(request.duration());
            assertThat(actual.updatedAt()).isGreaterThan(updatedAt);
        } else {
            var actual = catchThrowable(() -> this.componentUnderTest.editMetadata(videoId, request));
            assertThat(actual).isNotNull()
                    .isInstanceOf(VideoNotFoundException.class)
                    .hasMessage(VideoNotFoundException.of(videoId).getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void hideVideoTest(boolean exists) throws VideoNotFoundException {
        // Arrange
        var id = UUID.randomUUID();
        var videoFile = new MockMultipartFile("video", "video.txt", "text/plain", "video.txt".getBytes());
        Long updatedAt = null;
        if (exists) {
            var video = this.componentUnderTest.postVideo(videoFile);
            id = video.id();
            updatedAt = video.updatedAt();
        }

        // Act + Assert
        var videoId = id;
        if (exists) {
            this.componentUnderTest.hideVideo(videoId);
            var actual = this.videoRepository.findById(videoId).orElseThrow();
            assertThat(actual.isHidden()).isTrue();
            assertThat(actual.getUpdatedAt()).isGreaterThan(updatedAt);
        } else {
            var actual = catchThrowable(() -> this.componentUnderTest.hideVideo(videoId));
            assertThat(actual).isNotNull()
                    .isInstanceOf(VideoNotFoundException.class)
                    .hasMessage(VideoNotFoundException.of(videoId).getMessage());
        }
    }

}