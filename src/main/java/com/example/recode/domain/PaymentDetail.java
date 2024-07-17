package com.example.recode.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_detail_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PaymentDetail {

    @Id
    @Column(name = "payment_detail_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentDetailId;

    @Column(name = "payment_id", nullable = false)
    private long paymentId;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "payment_detail_status", nullable = false)
    private String paymentDetailStatus;

    @Column(name = "payment_detail_price", nullable = false)
    private int paymentDetailPrice;

    @Builder
    public PaymentDetail(long paymentDetailId, long paymentId, long productId, String paymentDetailStatus, int paymentDetailPrice) {
        this.paymentDetailId = paymentDetailId;
        this.paymentId = paymentId;
        this.productId = productId;
        this.paymentDetailStatus = paymentDetailStatus;
        this.paymentDetailPrice = paymentDetailPrice;
    }

    public void updateStatus(String paymentDetailStatus){
        this.paymentDetailStatus = paymentDetailStatus;
    }
}
