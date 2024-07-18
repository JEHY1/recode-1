package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class MyWrittenReviewResponse {

    private Page<MyWrittenReview> myWrittenReviewList;
    private int totalPages;

    @Builder
    public MyWrittenReviewResponse(Page<MyWrittenReview> myWrittenReviewList, int totalPages) {
        this.myWrittenReviewList = myWrittenReviewList;
        this.totalPages = totalPages;
    }
}
