package com.example.recode.controller.api;

import com.example.recode.dto.*;
import com.example.recode.service.PaymentService;
import com.example.recode.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchApiController {

    private final PaymentService paymentService;
    private final ProductService productService;

    //관리자 상품 이름 검색기능
    @PostMapping("/admin/getIncludeNameList")
    public ResponseEntity<List<ProductNameForm>> searchProduct(@RequestBody IncludeNameListRequest request){
        return ResponseEntity.ok()
                .body(paymentService.getPaymentDetailProductInProductName(request.getProductName()));
    }

    //사용자이름검색기능
    @PostMapping("/admin/getIncludeUserRealNameList")
    public ResponseEntity<List<UserRealNameForm>> searchUserName(@RequestBody IncludeUserRealNameListRequest request){
        return ResponseEntity.ok()
                .body(paymentService.getUserNameInfoInUsername(request.getUsername()));
    }

    //서버 시간 확인 기능
    @PostMapping("/admin/getServerDate")
    public ResponseEntity<DateForm> getServerDate(@RequestBody GetPeriodRequest request){
        return ResponseEntity.ok()
                .body(paymentService.getServerDate(request.getUnitPeriod()));
    }

    //사용자 상품 이름 검색
    @PostMapping("/getIncludeNameList")
    public ResponseEntity<List<ProductNameForm>> searchProductNameIn(@RequestBody IncludeNameListRequest request){
        return ResponseEntity.ok()
                .body(productService.searchProductNameContaining(request.getProductName()));
    }
}


