package com.example.recode.controller.api;

import com.example.recode.domain.Payment;
import com.example.recode.dto.CleanCartRequest;
import com.example.recode.dto.CleanPaymentListRequest;
import com.example.recode.dto.DeleteCartResponse;
import com.example.recode.dto.PaymentRequest;
import com.example.recode.service.CartService;
import com.example.recode.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PaymentApiController {

    private final PaymentService paymentService;
    private final CartService cartService;
    
    //결제 요청 처리
    @PostMapping("/user/payment")
    public ResponseEntity<Payment> pay(@RequestBody PaymentRequest request, Principal principal){

        System.err.println(request);
        return ResponseEntity.ok()
                .body(paymentService.payment(request, principal));
    }

    //결제 명세서중 품절 상품 삭제 처리
    @PostMapping("/user/payment/cleanList")
    public ResponseEntity<DeleteCartResponse> cleanPaymentList(@RequestBody CleanPaymentListRequest request){
        return ResponseEntity.ok()
                .body(new DeleteCartResponse(cartService.cleanCart(request)));
    }
}
