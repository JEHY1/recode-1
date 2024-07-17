package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaymentRequest {

    private int paymentPrice;
    private String paymentType;
    private String paymentBank;
    private String paymentDepositor;
    private String paymentAccountNumber;

    private String paymentCard;
    private int paymentInstallment;
    private String paymentCardNumber;
    private String paymentPhoneCompany;
    private String paymentMicropaymentPhone;

    private String paymentPostalCode;
    private String paymentAddress;
    private String paymentRecipientName;
    private String paymentRecipientPhone;
    private String deliveryRequest;

    private String deliveryBoxNum;
    private String frontDoorSecret;
    private int deliveryFee;
    private List<Long> productIds;
    private List<Long> cartIds;
}
