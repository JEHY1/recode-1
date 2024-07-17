package com.example.recode.controller.api;

import com.example.recode.domain.Cart;
import com.example.recode.dto.*;
import com.example.recode.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    //장바구니 상품 추가
    @PostMapping("/user/addCart")
    public ResponseEntity<Cart> addCart(@RequestBody AddCartRequest request, Principal principal){
        return ResponseEntity.ok()
                .body(cartService.save(request, principal));

    }

    //장바구니 상품 삭제
    @DeleteMapping("/user/cart")
    public ResponseEntity<Void> deleteCart(@RequestBody DeleteCartRequest request){

        cartService.delete(request);

        return ResponseEntity.ok()
                .build();
    }

    //장바구니 리스트 삭제
    @DeleteMapping("/user/cartList")
    public ResponseEntity<Void> deleteCartList(@RequestBody DeleteCartListRequest request){

        cartService.deleteList(request);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/user/cart/cleanList") //품절 상품 지우기
    public ResponseEntity<DeleteCartResponse> cleanCart(@RequestBody CleanCartRequest request){

        return ResponseEntity.ok()
                .body(new DeleteCartResponse(cartService.cleanCart(request)));
    }
}
