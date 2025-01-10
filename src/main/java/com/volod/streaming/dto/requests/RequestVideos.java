package com.volod.streaming.dto.requests;

import com.volod.streaming.model.Video;
import com.volod.streaming.model.VideoSpecs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import static java.util.Objects.nonNull;

public record RequestVideos(
        @Nullable String title,
        @Nullable String director,
        @Nullable String mainActor,
        @Nullable Integer page
) {

    public Specification<Video> toSpecification() {
        var spec = Specification.<Video>where(null);

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

    public Pageable toPageable() {
        return PageRequest.of(this.getPage(), 50);
    }

}
