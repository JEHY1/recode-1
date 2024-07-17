package com.example.recode.controller.user;

import com.example.recode.domain.Address;
import com.example.recode.dto.AddressManagementViewRequest;
import com.example.recode.dto.AtDetailPaymentViewRequest;
import com.example.recode.dto.PaymentViewRequest;
import com.example.recode.service.AddressService;
import com.example.recode.service.CartService;
import com.example.recode.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final AddressService addressService;
    private final CartService cartService;
    private final PaymentService paymentService;

    //사용자 주소 리스트 페이지
    @GetMapping("/user/address/list")
    public String addressList(Model model, Principal principal){

        addressService.addressListEasyView(principal);
        model.addAttribute("addresses", addressService.addressListEasyView(principal));

        return "users/deliveryAddressList";
    }

    //사용자 주소 생성 및 수정 페이지
    @PostMapping("/user/address/managementView")
    public String deliveryAddressManagement(AddressManagementViewRequest request, Model model){

        if(request.getAddressId() != null){
            Address address = addressService.findAddressByAddressId(request.getAddressId());
            model.addAttribute("address", address);
        }

        return "users/deliveryAddressManagement";
    }

    //장바구니 페이지
    @GetMapping("/user/cart")
    public String cartList(Model model, Principal principal){

        model.addAttribute("carts", cartService.findCartByPrincipal(principal));

        return "users/cart";
    }

    //결제 페이지
    @PostMapping("/user/paymentView")
    public String paymentView(Model model, PaymentViewRequest request, Principal principal){

        model.addAttribute("addressNicknameList", addressService.getAddressNicknameList(principal));
        model.addAttribute("cartInfoList", cartService.getCartInfoList(request));
        model.addAttribute("defaultAddressInfo", addressService.findAddressByUserIdAndAddressDefault(principal));

        return "users/payment";
    }

    //상품 상세 페이지 -> 결제 페이지 상품 판매 여부 확인
    @PostMapping("/user/paymentView2")
    public String paymentView2(Model model, AtDetailPaymentViewRequest request, Principal principal){

        model.addAttribute("addressNicknameList", addressService.getAddressNicknameList(principal));
        model.addAttribute("cartInfoList", cartService.getCartInfoList(request));
        model.addAttribute("defaultAddressInfo", addressService.findAddressByUserIdAndAddressDefault(principal));

        return "users/payment";
    }

    //마이페이지
    @GetMapping("/user/myPage")
    public String myPage(Model model, Principal principal){

        model.addAttribute("deliveryInfo", paymentService.getMyPageInfo(principal));
        return "/users/myPage";
    }


    //주문내역 조회
    @GetMapping("/user/orderCheck")
    public String orderCheck(Model model, Principal principal, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam(required = false) Integer unitPeriod, @PageableDefault(page = 0, size = 5) Pageable pageable){

        model.addAttribute("orderCheckInfos", paymentService.orderCheck(principal, startDate, endDate, unitPeriod, pageable));
        model.addAttribute("period", paymentService.getPeriod(unitPeriod, startDate, endDate));
        model.addAttribute("unitPeriod", unitPeriod);

        return "/users/orderCheck";
    }

}
