package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailManagerViewResponse {

    private long paymentDetailId;
    private String productRepImgSrc;
    private String productName;
    private int productPrice;
    private String paymentDetailStatus;

    @Builder
    public OrderDetailManagerViewResponse(long paymentDetailId, String productRepImgSrc, String productName, int productPrice, String paymentDetailStatus) {
        this.paymentDetailId = paymentDetailId;
        this.productRepImgSrc = productRepImgSrc;
        this.productName = productName;
        this.productPrice = productPrice;
        this.paymentDetailStatus = paymentDetailStatus;
    }
}
