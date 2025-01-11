package com.volod.streaming.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = VideoEngagement.TABLE_NAME)
public class VideoEngagement extends AbstractPersistable {
    public static final String TABLE_NAME = "video_engagements";

    @Column(name = "video_id")
    private UUID videoId;
    @Column(name = "impressions")
    private int impressions;
    @Column(name = "views")
    private int views;

    public VideoEngagement() {
    }

    public VideoEngagement(UUID videoId, int impressions, int views) {
        this.videoId = videoId;
        this.impressions = impressions;
        this.views = views;
    }

    public VideoEngagement(Video video) {
        this(
                video.id,
                0,
                0
        );
    }

    public Map<VideoEngagementType, Integer> getEngagements() {
        var mappings = new EnumMap<VideoEngagementType, Integer>(VideoEngagementType.class);

        mappings.put(VideoEngagementType.IMPRESSION, this.impressions);
        mappings.put(VideoEngagementType.VIEW, this.views);

        return mappings;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        var oEffectiveClass = o instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        var thisEffectiveClass = this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        var that = (VideoEngagement) o;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ?
                proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : this.getClass().hashCode();
    }

}
