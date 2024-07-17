package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderCheckResponse {

    private LocalDateTime orderDate;
    private List<ProductOrderCheckResponse> paymentDetails;

    @Builder
    public OrderCheckResponse(LocalDateTime orderDate, List<ProductOrderCheckResponse> paymentDetails) {
        this.orderDate = orderDate;
        this.paymentDetails = paymentDetails;
    }
}
