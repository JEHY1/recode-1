package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class MyWritableReviewResponse {
    private Page<MyWritableReview> myWritableReviewList;
    private int totalPage;

    @Builder
    public MyWritableReviewResponse(Page<MyWritableReview> myWritableReviewList, int totalPage) {
        this.myWritableReviewList = myWritableReviewList;
        this.totalPage = totalPage;
    }
}
