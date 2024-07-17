package com.example.recode.controller.api;

import com.example.recode.domain.Address;
import com.example.recode.dto.AddressDeleteRequest;
import com.example.recode.dto.GetAddressInfoRequest;
import com.example.recode.dto.ManagementAddressRequest;
import com.example.recode.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AddressApiController {

    private final AddressService addressService;

    //주소 정보 신규 생성 및 수정
    @PostMapping("/user/address/management")
    public ResponseEntity<Address> addressManagement(@RequestBody ManagementAddressRequest request, Principal principal){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.managementAddress(request, principal));
    }

    //주소 정보 불러오기
    @PostMapping("/user/address/getAddressInfo")
    public ResponseEntity<Address> responseAddressInfo(@RequestBody GetAddressInfoRequest request, Principal principal){

        if(principal == null){
            return ResponseEntity.ok()
                    .body(addressService.findAddressByAddressId(request.getAddressId()));
        }
        else{
            return ResponseEntity.ok()
                    .body(addressService.findAddressByAddressId(request.getAddressId()));
        }
    }

    //주소 삭제
    @DeleteMapping("/user/address/delete")
    public ResponseEntity<Void> deleteAddress(@RequestBody AddressDeleteRequest request){

        addressService.deleteAddressByAddressId(request.getAddressId());
        return ResponseEntity.ok()
                .build();
    }
}
