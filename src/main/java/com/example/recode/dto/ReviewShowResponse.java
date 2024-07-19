package com.example.recode.dto;

import com.example.recode.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewShowResponse {

    private Long reviewId;
    private Long productId;
    private Long paymentDetailId;
    private String reviewTitle;
    private String reviewContent;
    private LocalDateTime reviewCreateDate;
    private int reviewScore;
    private List<String> reviewImg;


    @Builder
    public ReviewShowResponse(Review review, List<String> reviewImg) {
        this.reviewId = review.getReviewId();
        this.productId = review.getProductId();
        this.paymentDetailId = review.getPaymentDetailId();
        this.reviewTitle = review.getReviewTitle();
        this.reviewContent = review.getReviewContent();
        this.reviewCreateDate = review.getReviewCreateDate();
        this.reviewScore = review.getReviewScore();
        this.reviewImg = reviewImg;
    }

    public ReviewShowResponse(long productId, long paymentDetailId){
        this.productId = productId;
        this.paymentDetailId = paymentDetailId;
    }
}
