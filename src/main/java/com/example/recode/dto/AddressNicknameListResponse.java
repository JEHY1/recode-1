package com.example.recode.dto;

import com.example.recode.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressNicknameListResponse {

    private long addressId;
    private String addressNickname;
    private int addressDefault;

    @Builder
    public AddressNicknameListResponse(Address address) {
        this.addressId = address.getAddressId();
        this.addressNickname = address.getAddressNickname();
        this.addressDefault = address.getAddressDefault();
    }
}
