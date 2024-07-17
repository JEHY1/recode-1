package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReviewDto {
    private Long reviewId;
    private Long userId;
    private Long productId;
    private String reviewTitle;
    private String reviewContent;
    private LocalDate reviewCreateDate;
    private int reviewViews;
    private int reviewScore;
    private List<MultipartFile> files;
}

