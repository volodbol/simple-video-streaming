package com.volod.streaming.model;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class VideoSpecs {

    public static Specification<Video> hasTitle(String title) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(Video_.title)), title.toLowerCase() + "%");
    }

    public static Specification<Video> hasDirector(String director) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(Video_.director)), director.toLowerCase() + "%");
    }

    public static Specification<Video> hasMainActor(String actor) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(Video_.mainActor)), actor.toLowerCase() + "%");
    }

}
