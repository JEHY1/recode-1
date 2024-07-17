package com.example.recode.domain;

import com.example.recode.dto.AdminAddressRequest;
import com.example.recode.dto.ManagementAddressRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    @Id
    @Column(name = "address_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "address_postal_code", nullable = false)
    private String addressPostalCode;

    @Column(name = "address_road_name_address", nullable = false)
    private String addressRoadNameAddress;

    @Column(name = "address_detail_address", nullable = false)
    private String addressDetailAddress;

    @Column(name = "address_recipient_name", nullable = false)
    private String addressRecipientName;

    @Column(name = "address_recipient_phone", nullable = false)
    private String addressRecipientPhone;

    @Column(name = "address_delivery_request", nullable = false)
    private String addressDeliveryRequest;

    @Column(name = "address_front_door_secret")
    private String addressFrontDoorSecret;

    @Column(name = "address_delivery_box_num")
    private String addressDeliveryBoxNum;

    @Column(name = "address_nickname", nullable = false)
    private String addressNickname;

    @Column(name = "address_default", nullable = false)
    private int addressDefault;
    @Builder
    public Address(long addressId, long userId, String addressPostalCode, String addressRoadNameAddress, String addressDetailAddress, String addressRecipientName, String addressRecipientPhone, String addressDeliveryRequest, String addressFrontDoorSecret, String addressDeliveryBoxNum, String addressNickname, int addressDefault) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressPostalCode = addressPostalCode;
        this.addressRoadNameAddress = addressRoadNameAddress;
        this.addressDetailAddress = addressDetailAddress;
        this.addressRecipientName = addressRecipientName;
        this.addressRecipientPhone = addressRecipientPhone;
        this.addressDeliveryRequest = addressDeliveryRequest;
        this.addressFrontDoorSecret = addressFrontDoorSecret;
        this.addressDeliveryBoxNum = addressDeliveryBoxNum;
        this.addressNickname = addressNickname;
        this.addressDefault = addressDefault;
    }

    public Address update(ManagementAddressRequest request){
        this.addressPostalCode = request.getPostalCode();
        this.addressRoadNameAddress = request.getRoadNameAddress();
        this.addressDetailAddress = request.getDetailAddress();
        this.addressRecipientName = request.getRecipientName();
        this.addressRecipientPhone = request.getRecipientPhone();
        this.addressDeliveryRequest = request.getDeliveryRequest();
        this.addressFrontDoorSecret = request.getFrontDoorSecret();
        this.addressDeliveryBoxNum = request.getDeliveryBoxNum();
        this.addressNickname = request.getAddressNickname();
        this.addressDefault = request.getAddressDefault();

        return this;
    }

    public void updateDefault(){
        this.addressDefault = 0;
    }

    public Address updateAdminAddress(AdminAddressRequest dto) { // 관리자 페이지에서 Address 수정

        this.addressPostalCode = dto.getAddressPostalCode();
        this.addressRoadNameAddress = dto.getAddressRoadNameAddress();
        this.addressDetailAddress = dto.getAddressDetailAddress();
        this.addressRecipientName = dto.getAddressRecipientName();
        this.addressRecipientPhone = dto.getAddressRecipientPhone();
        this.addressDeliveryRequest = dto.getAddressDeliveryRequest();
        this.addressNickname = dto.getAddressNickname();
        this.addressDefault = dto.getAddressDefault();

        return this;
    }
}
