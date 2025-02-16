package com.kijiri.aurora.api.repository;

import com.kijiri.aurora.api.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
}
