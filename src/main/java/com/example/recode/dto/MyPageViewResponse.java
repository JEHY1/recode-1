package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageViewResponse {
    private int paymentReadyCount = 0;
    private int deliveryReadyCount = 0;
    private int deliveryCount = 0;
    private int deliveryCompleteCount = 0;
    private int cancelCount = 0;
//    private int exchangeCount = 0;
    private int returnCount = 0;

    @Builder
    public MyPageViewResponse(int paymentReadyCount, int deliveryReadyCount, int deliveryCount, int deliveryCompleteCount, int cancelCount, int exchangeCount, int returnCount) {
        this.paymentReadyCount = paymentReadyCount;
        this.deliveryReadyCount = deliveryReadyCount;
        this.deliveryCount = deliveryCount;
        this.deliveryCompleteCount = deliveryCompleteCount;
        this.cancelCount = cancelCount;
//        this.exchangeCount = exchangeCount;
        this.returnCount = returnCount;
    }
}
