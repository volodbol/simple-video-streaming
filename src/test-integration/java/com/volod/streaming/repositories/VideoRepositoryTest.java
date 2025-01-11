package com.volod.streaming.repositories;

import com.volod.streaming.TestcontainersConfiguration;
import com.volod.streaming.domain.dto.requests.RequestVideos;
import com.volod.streaming.domain.model.AbstractAuditPersistable_;
import com.volod.streaming.domain.model.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void findAllPageableTest() {
        // Arrange
        var videos = IntStream.range(0, 10).mapToObj(i -> Video.random(true)).collect(Collectors.toList());
        videos.addAll(IntStream.range(0, 30).mapToObj(i -> Video.random(false)).toList());
        this.videoRepository.saveAll(videos);

        // Act + Assert
        var pageRequest = PageRequest.of(0, 10, Sort.by(DESC, AbstractAuditPersistable_.UPDATED_AT));
        assertThat(this.videoRepository.findAllByHiddenIsFalse(pageRequest)).hasSize(10).noneMatch(Video::isHidden)
                .extracting(Video::getUpdatedAt).isNotEqualTo(0).isSortedAccordingTo(Comparator.reverseOrder());
        pageRequest = pageRequest.next();
        assertThat(this.videoRepository.findAllByHiddenIsFalse(pageRequest)).hasSize(10).noneMatch(Video::isHidden)
                .extracting(Video::getUpdatedAt).isNotEqualTo(0).isSortedAccordingTo(Comparator.reverseOrder());
        pageRequest = pageRequest.next();
        var lastSlice = this.videoRepository.findAllByHiddenIsFalse(pageRequest);
        assertThat(lastSlice.isLast()).isTrue();
        assertThat(lastSlice).hasSize(10).noneMatch(Video::isHidden)
                .extracting(Video::getUpdatedAt).isNotEqualTo(0).isSortedAccordingTo(Comparator.reverseOrder());
    }

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