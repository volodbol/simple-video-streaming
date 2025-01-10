package com.volod.streaming.repositories;

import com.volod.streaming.model.VideoEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoEngagementRepository extends JpaRepository<VideoEngagement, UUID> {

}
