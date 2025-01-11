package com.volod.streaming.domain.dto.requests;

import com.volod.streaming.domain.model.Video;
import com.volod.streaming.domain.model.VideoSpecs;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import static java.util.Objects.nonNull;

public record RequestVideos(
        @Nullable String title,
        @Nullable String director,
        @Nullable String mainActor,
        @Nullable Integer page,
        @Nullable @Positive @Max(100) Integer size
) {

    public Specification<Video> toSpecification() {
        var spec = Specification.where(VideoSpecs.listed());

        if (nonNull(this.title)) {
            spec = spec.and(VideoSpecs.hasTitle(this.title));
        }
        if (nonNull(this.director)) {
            spec = spec.and(VideoSpecs.hasDirector(this.director));
        }
        if (nonNull(this.mainActor)) {
            spec = spec.and(VideoSpecs.hasMainActor(this.mainActor));
        }

        return spec;
    }

    public int getPage() {
        return nonNull(this.page) ? this.page : 0;
    }

    public int getSize() {
        return nonNull(this.size) ? this.size : 50;
    }

    public Pageable toPageable() {
        return PageRequest.of(this.getPage(), this.getSize());
    }

}
