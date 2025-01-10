package com.volod.streaming;

import com.volod.streaming.dto.requests.RequestVideos;
import com.volod.streaming.model.Video;
import com.volod.streaming.repositories.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestcontainersConfiguration.class)
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void findAllSpecificationTest() {
        // Arrange
        this.videoRepository.saveAll(Video.dummies1());

        // Act + Assert
        var pageRequest = PageRequest.of(0, 3);
        assertThat(this.videoRepository.findAll(new RequestVideos(null, null, null, null).toSpecification(), pageRequest)).hasSize(3);
        assertThat(this.videoRepository.findAll(new RequestVideos("Dune", null, null, null).toSpecification(), pageRequest)).hasSize(2);
        assertThat(this.videoRepository.findAll(new RequestVideos("Dune 2", null, null, null).toSpecification(), pageRequest)).hasSize(1);
        assertThat(this.videoRepository.findAll(new RequestVideos(null, "Mart", null, null).toSpecification(), pageRequest)).hasSize(2);
        assertThat(this.videoRepository.findAll(new RequestVideos(null, "Denis", "Timoth", null).toSpecification(), pageRequest)).hasSize(2);
        assertThat(this.videoRepository.findAll(new RequestVideos("Dun", "Denis", "Timoth", null).toSpecification(), pageRequest)).hasSize(2);
        assertThat(this.videoRepository.findAll(new RequestVideos("Heat", null, null, null).toSpecification(), pageRequest)).hasSize(1);
    }

}