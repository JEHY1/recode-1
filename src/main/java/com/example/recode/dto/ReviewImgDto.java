package com.example.recode.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReviewImgDto {
    private Long reviewId;
    private Long userId;
    private Long productId;
    private String reviewTitle;
    private String reviewContent;
    private LocalDate reviewCreateDate;
    private int reviewViews;
    private int reviewScore;
    private List<String> imgUrls;
}
