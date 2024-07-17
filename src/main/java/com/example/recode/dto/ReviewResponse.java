package com.example.recode.dto;

import com.example.recode.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReviewResponse {

    private long reviewId;
    private long userId;
    private String username;
    private String productName;
    private String reviewTitle;
    private LocalDateTime reviewCreateDate;
    private int reviewScore;
    private int reviewViews;

    public ReviewResponse(Review review, String username, String productName) {
        this.reviewId = review.getReviewId();
        this.userId = review.getUserId();
        this.username = username;
        this.productName = productName;
        this.reviewTitle = review.getReviewTitle();
        this.reviewCreateDate = review.getReviewCreateDate();
        this.reviewScore = review.getReviewScore();
        this.reviewViews = review.getReviewViews();
    }
}
