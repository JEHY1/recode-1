package com.example.recode.dto;

import com.example.recode.domain.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressEasyViewResponse {

    private long addressId;
    private String addressNickname;
    private String roadAndDetailAddress;
    private String recipientPhone;
    private String deliveryRequest;
    private int addressDefault;

    public AddressEasyViewResponse(Address address){
        this.addressId = address.getAddressId();
        this.addressNickname = address.getAddressNickname();
        this.roadAndDetailAddress = address.getAddressRoadNameAddress() + ", " + address.getAddressDetailAddress();
        this.recipientPhone = address.getAddressRecipientPhone().substring(0, 3) + "-" + address.getAddressRecipientPhone().substring(3, 7) + "-" + address.getAddressRecipientPhone().substring(7, 11);
        this.deliveryRequest = address.getAddressDeliveryRequest();
        this.addressDefault = address.getAddressDefault();
    }

}
