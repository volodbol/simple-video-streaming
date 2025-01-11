package com.volod.streaming.repositories;

import com.volod.streaming.domain.model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID>, JpaSpecificationExecutor<Video> {
    Slice<Video> findAllByHiddenIsFalse(Pageable pageable);
}
