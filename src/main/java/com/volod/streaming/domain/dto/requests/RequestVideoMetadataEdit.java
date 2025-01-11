package com.volod.streaming.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record RequestVideoMetadataEdit(
        @NotEmpty String title,
        @NotEmpty String director,
        @NotEmpty String mainActor,
        @NotEmpty String genre,
        @Positive int duration
) {
    public static RequestVideoMetadataEdit hardcoded() {
        return new RequestVideoMetadataEdit(
                "Nice title",
                "Handsome director",
                "Amazing main actor",
                "Unique genre",
                7200
        );
    }
}
