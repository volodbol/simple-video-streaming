package com.volod.streaming.repositories;

import com.volod.streaming.domain.model.VideoEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoEngagementRepository extends JpaRepository<VideoEngagement, UUID> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE VideoEngagement SET impressions = impressions + 1 WHERE videoId = ?1")
    void updateImpression(UUID videoId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE VideoEngagement SET views = views + 1 WHERE videoId = ?1")
    void updateViews(UUID videoId);

    Optional<VideoEngagement> findByVideoId(UUID videoId);
}
