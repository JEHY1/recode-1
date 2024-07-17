package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminAddressRequest {

    private long addressId;
    private String addressPostalCode;
    private String addressRoadNameAddress;
    private String addressDetailAddress;
    private String addressRecipientName;
    private String addressRecipientPhone;
    private String addressDeliveryRequest;
    private String addressNickname;
    private int addressDefault;
}
