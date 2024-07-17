package com.example.recode.repository;

import com.example.recode.domain.QnA;
import com.example.recode.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Page<Review>> findByProductIdIn(List<Long> productIds, Pageable pageable);
    Optional<Page<Review>> findByReviewTitleContaining(String searchKeyword, Pageable pageable);
    Optional<Page<Review>> findByUserIdIn(List<Long> userIds, Pageable pageable);
}
