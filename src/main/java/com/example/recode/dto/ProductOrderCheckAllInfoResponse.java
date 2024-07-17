package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ProductOrderCheckAllInfoResponse {

    private Page<OrderCheckResponse> orderCheckInfoList;
    private int nonDepositCount;
    private int deliveryReadyCount;
    private int deliveryInCount;
    private int deliveryCompleteCount;
    private int totalPage;

    @Builder
    public ProductOrderCheckAllInfoResponse(Page<OrderCheckResponse> orderCheckInfoList, int nonDepositCount, int deliveryReadyCount, int deliveryInCount, int deliveryCompleteCount, int totalPage) {
        this.orderCheckInfoList = orderCheckInfoList;
        this.nonDepositCount = nonDepositCount;
        this.deliveryReadyCount = deliveryReadyCount;
        this.deliveryInCount = deliveryInCount;
        this.deliveryCompleteCount = deliveryCompleteCount;
        this.totalPage = totalPage;
    }
}
