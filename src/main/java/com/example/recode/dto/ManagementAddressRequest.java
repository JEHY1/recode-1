package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ManagementAddressRequest {

    private Long addressId;
    private String addressNickname;
    private String postalCode;
    private String roadNameAddress;
    private String detailAddress;
    private String recipientName;
    private String recipientPhone;
    private String deliveryRequest;
    private String frontDoorSecret;
    private String deliveryBoxNum;
    private int addressDefault;
}
