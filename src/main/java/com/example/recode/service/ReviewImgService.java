package com.example.recode.service;

import com.example.recode.domain.ReviewImg;
import com.example.recode.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImgService {

    private final ReviewImgRepository reviewImgRepository;

    public List<ReviewImg> getReviewImgFindByReviewId(long reviewId) {
        return reviewImgRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found reviewimgs"));
    }
}
