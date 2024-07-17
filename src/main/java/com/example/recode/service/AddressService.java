package com.example.recode.service;

import com.example.recode.domain.Address;
import com.example.recode.dto.*;
import com.example.recode.repository.AddressRepository;
import com.example.recode.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    @Transactional
    public Address managementAddress(ManagementAddressRequest request, Principal principal){

        //기본 주소지로 설정했는지 확인 및 기존에 기본 주소지가 있는지 확인
        if(request.getAddressDefault() == 1 && findAddressByUserIdAndAddressDefault(userService.getUserId(principal)) != null){

            //기본 주소지로 설정된 주소의 기본주소지 설정 해제
            findAddressByUserIdAndAddressDefault(userService.getUserId(principal)).updateDefault();
        }

        if(request.getAddressId() == null){//신규 등록
            return addressRepository.save(Address.builder()
                    .userId(userService.getUserId(principal))
                    .addressPostalCode(request.getPostalCode())
                    .addressRoadNameAddress(request.getRoadNameAddress())
                    .addressDetailAddress(request.getDetailAddress())
                    .addressRecipientName(request.getRecipientName())
                    .addressRecipientPhone(request.getRecipientPhone())
                    .addressDeliveryRequest(request.getDeliveryRequest())
                    .addressFrontDoorSecret(!request.getFrontDoorSecret().equals("") ? request.getFrontDoorSecret() : null)
                    .addressDeliveryBoxNum(!request.getDeliveryBoxNum().equals("") ? request.getDeliveryBoxNum() : null)
                    .addressNickname(request.getAddressNickname())
                    .addressDefault(request.getAddressDefault())
                    .build());
        }
        else{ //수정
            return findAddressByAddressId(request.getAddressId()).update(request);
        }

    }

    //로그인한 유저의 배송지 정보(AddressEasyViewResponse 참조) 리스트 반환(기본 배송지설정 순으로 정렬 : 기본배송지 1개 또는 없음)
    public List<AddressEasyViewResponse> addressListEasyView(Principal principal){
        List<AddressEasyViewResponse> easyViewList = findAddressByUserId(userService.getUserId(principal)).stream().map(AddressEasyViewResponse::new).collect(Collectors.toList());
//        List<AddressEasyViewResponse> easyViewList = findAddressByUserId(1L).stream().map(AddressEasyViewResponse::new).toList(); 그냥 toList() 하면 불변 리스트가 됨

        easyViewList.sort((o1, o2) -> o2.getAddressDefault() - o1.getAddressDefault());

        return easyViewList;
    }

    //로그인한 유저의 배송지 정보(설정 이름과 배송지 아이디) 리스트를 반환 기본 배송지로 정렬
    public List<AddressNicknameListResponse> getAddressNicknameList(Principal principal){

        List<AddressNicknameListResponse> list = findAddressByUserId(userService.getUserId(principal)).stream().map(AddressNicknameListResponse::new).collect(Collectors.toList());
        list.sort((address1, address2) -> address2.getAddressDefault() - address1.getAddressDefault());
        return list;
    }

    public Address findAddressByAddressId(long addressId){
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("not found address"));
    }

    //사용자의 기본 배송지로 설정된 Address 반환(address_default == 1 일 경우)
    public Address findAddressByUserIdAndAddressDefault(long userId){
        return addressRepository.findByUserIdAndAddressDefault(userId, 1)
                .orElse(null);
    }

    public Address findAddressByUserIdAndAddressDefault(Principal principal){
        return findAddressByUserIdAndAddressDefault(userService.getUserId(principal));
    }

    public List<Address> findAddressByUserId(long userId){
        return addressRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found address"));
    }

    public void deleteAddressByAddressId(long addressId){
        addressRepository.deleteById(addressId);
    }

    @Transactional
    public void updateAdminUserAddress(AdminUserRequest dto) { // 관리자 페이지에서 회원정보 수정

        userService.findById(dto.getUserId()).updateAdminUser(dto); // 관리자 페이지에서 User 수정

        if (dto.getAddresses() != null) {
            List<AdminAddressRequest> list = dto.getAddresses();
            list.forEach(address -> {
                findAddressByAddressId(address.getAddressId()).updateAdminAddress(address); // 관리자 페이지에서 Address 수정
            });
        }

    }
}
