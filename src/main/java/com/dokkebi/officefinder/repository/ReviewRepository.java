package com.dokkebi.officefinder.repository;

import com.dokkebi.officefinder.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  boolean existsByLeaseId(Long leaseId);

}