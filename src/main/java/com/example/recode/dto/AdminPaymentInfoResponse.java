package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AdminPaymentInfoResponse {

    private long paymentId;
    private String productName;
    private String userRealName;
    private int paymentPrice;
    private String paymentStatus;
    private LocalDateTime paymentDate;

    @Builder
    public AdminPaymentInfoResponse(long paymentId, String productName, String userRealName, int paymentPrice, String paymentStatus, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.productName = productName;
        this.userRealName = userRealName;
        this.paymentPrice = paymentPrice;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }
}
