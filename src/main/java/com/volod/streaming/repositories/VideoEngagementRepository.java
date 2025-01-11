package com.volod.streaming.repositories;

import com.volod.streaming.domain.model.VideoEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoEngagementRepository extends JpaRepository<VideoEngagement, UUID> {
    Optional<VideoEngagement> findByVideoId(UUID videoId);
}
