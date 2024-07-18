package com.example.recode.domain;

import com.example.recode.dto.ReviewSubmitRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", updatable = false)
    private long reviewId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "payment_detail_id", nullable = false)
    private long paymentDetailId;

    @Column(name = "review_title", nullable = false)
    private String reviewTitle;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @CreatedDate
    @Column(name = "review_create_date", nullable = false)
    private LocalDateTime reviewCreateDate;

    @Column(name = "review_score", nullable = false)
    private int reviewScore;

    @Column(name = "review_views", nullable = false)
    private int reviewViews;

    @Builder
    public Review(long reviewId, long userId, long productId, long paymentDetailId,  String reviewTitle, String reviewContent, LocalDateTime reviewCreateDate, int reviewScore, int reviewViews) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.productId = productId;
        this.paymentDetailId = paymentDetailId;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewCreateDate = reviewCreateDate;
        this.reviewScore = reviewScore;
        this.reviewViews = reviewViews;
    }

    public Review updateViews(){ //조회수 증가
        this.reviewViews++;
        return this;
    }

    public Review updateReview(ReviewSubmitRequest dto) {
        this.reviewTitle = dto.getReviewTitle();
        this.reviewContent = dto.getReviewContent();
        this.reviewScore = dto.getReviewScore();
        return this;
    }
}
