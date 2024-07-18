package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MyWritableReview {
    private long paymentDetailId;
    private long productId;
    private LocalDateTime paymentCreateDate;
    private String productRepImg;
    private String productName;

    @Builder
    public MyWritableReview(long paymentDetailId, long productId, LocalDateTime paymentCreateDate, String productRepImg, String productName) {
        this.paymentDetailId = paymentDetailId;
        this.productId = productId;
        this.paymentCreateDate = paymentCreateDate;
        this.productRepImg = productRepImg;
        this.productName = productName;
    }
}
