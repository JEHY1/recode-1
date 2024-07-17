package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOrderCheckResponse {

    private long paymentDetailId;
    private String productRepImgSrc;
    private String productName;
    private String paymentDetailStatus;
    private int paymentDetailPrice;

    @Builder
    public ProductOrderCheckResponse(long paymentDetailId, String productRepImgSrc, String productName, String paymentDetailStatus, int paymentDetailPrice) {
        this.paymentDetailId = paymentDetailId;
        this.productRepImgSrc = productRepImgSrc;
        this.productName = productName;
        this.paymentDetailStatus = paymentDetailStatus;
        this.paymentDetailPrice = paymentDetailPrice;
    }
}
