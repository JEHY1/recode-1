package com.example.recode.dto;

import com.example.recode.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReviewPhotoResponse {

    private long reviewId;
    private String username;
    private String reviewTitle;
    private LocalDateTime reviewCreateDate;
    private int reviewScore;
    private int reviewViews;
    private String reviewImgSrc;

    public ReviewPhotoResponse(Review review, String username, String reviewImgSrc) {
        this.reviewId = review.getReviewId();
        this.username = username;
        this.reviewTitle = review.getReviewTitle();
        this.reviewCreateDate = review.getReviewCreateDate();
        this.reviewScore = review.getReviewScore();
        this.reviewViews = review.getReviewViews();
        this.reviewImgSrc = reviewImgSrc;
    }
}
