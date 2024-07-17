package com.example.recode.dto;

import com.example.recode.domain.Review;
import lombok.Data;

import java.util.List;

@Data
public class ReviewWithImagesResponse {
    private Review review;
    private List<String> imgUrls;

    public ReviewWithImagesResponse(Review review, List<String> imgUrls) {
        this.review = review;
        this.imgUrls = imgUrls;
    }
}
