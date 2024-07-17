package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderManageRequest {

    private long paymentDetailId;
    private String paymentDetailStatus;
    private String paymentDetailStatusRequest;
}
