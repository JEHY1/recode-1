package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentCartInfoViewResponse {

    private Long cartId;
    private long productId;
    private String productRepImgSrc;
    private int productRegularPrice;
    private Integer productDiscountPrice;
    private String productName;

    @Builder
    public PaymentCartInfoViewResponse(Long cartId, long productId, String productRepImgSrc, int productRegularPrice, Integer productDiscountPrice, String productName) {
        this.cartId = cartId;
        this.productId = productId;
        this.productRepImgSrc = productRepImgSrc;
        this.productRegularPrice = productRegularPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.productName = productName;
    }
}
