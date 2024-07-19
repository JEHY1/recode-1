package com.example.recode.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_img_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id", updatable = false)
    private long reviewImgId;

    @Column(name = "review_id", nullable = false)
    private long reviewId;

    @Column(name = "review_img_src", nullable = false)
    private String reviewImgSrc;

    @Builder
    public ReviewImg(long reviewImgId, long reviewId, String reviewImgSrc) {
        this.reviewImgId = reviewImgId;
        this.reviewId = reviewId;
        this.reviewImgSrc = reviewImgSrc;
    }
}
