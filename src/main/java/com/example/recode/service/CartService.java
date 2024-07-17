package com.example.recode.service;

import com.example.recode.domain.Cart;
import com.example.recode.domain.Product;
import com.example.recode.dto.*;
import com.example.recode.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;

    //장바구니 등록
    public Cart save(AddCartRequest request, Principal principal){

        long userId = userService.getUserId(principal);

        //상품이 장바구니에 등록되어 있는지 확인
        Cart cart = cartRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElse(null);

        //등록되어있지 않을 경우 등록
        if(cart == null){
            return cartRepository.save(Cart.builder()
                    .productId(request.getProductId())
                    .userId(userId)
                    .build());
        }
        //이미 등록되어있는 경우 미등록
        else{
            return cart;
        }
    }

    //List<Long> cartId 로 장바구니 정보 가져오기
    public List<PaymentCartInfoViewResponse> getCartInfoList(PaymentViewRequest request){

        //반환할 리스트
        List<PaymentCartInfoViewResponse> list = new ArrayList<>();

        //검색된 장바구니 정보를 PaymentCartInfoViewResponse 로 변환, 리스트 담기
        request.getCartId().forEach(cartId -> {
            Product product = findProductByCartId(cartId);
            list.add(PaymentCartInfoViewResponse.builder()
                    .cartId(cartId)
                    .productId(product.getProductId())
                    .productRegularPrice(product.getProductRegularPrice())
                    .productDiscountPrice(product.getProductDiscountPrice())
                    .productRepImgSrc(product.getProductRepresentativeImgSrc())
                    .productName(product.getProductName())
                    .build());
        });

        return list;
    }

    //상품 상세 페이지 -> 결제 페이지
    public List<PaymentCartInfoViewResponse> getCartInfoList(AtDetailPaymentViewRequest request){
        List<PaymentCartInfoViewResponse> list = new ArrayList<>();
        Product product = productService.findProductByProductId(request.getProductId());
        list.add(PaymentCartInfoViewResponse.builder()
                .cartId(null)
                .productId(product.getProductId())
                .productRegularPrice(product.getProductRegularPrice())
                .productDiscountPrice(product.getProductDiscountPrice())
                .productRepImgSrc(product.getProductRepresentativeImgSrc())
                .productName(product.getProductName())
                .build());

        return list;
    }

    public Cart findCartByCartId(long cartId){
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("not found cart"));
    }

    public Product findProductByCartId(long cartId){
        return productService.findProductByProductId(findCartByCartId(cartId).getProductId());
    }

    public void delete(DeleteCartRequest request){
        cartRepository.deleteById(request.getCartId());
    }
    
    //장바구니 아이디 리스트(List<Long> cartId) 로 장바구니 삭제
    public void deleteList(DeleteCartListRequest request){
        request.getCartIds().forEach(cartId -> cartRepository.deleteById(cartId));
    }

    //결제 페이지 결제시 포함된 상품 아이디를 상품 테이블에 검색 -> 품절상품 리스트를 반환
    public List<Product> cleanCart(CleanPaymentListRequest request){
        List<Long> productIds = new ArrayList<>();
        productIds.add(request.getProductId());
        List<Product> products = productService.findProductByProductIdInAndProductSold(productIds);

        return products;
    }

    //장바구니에 포함된 상품중 품절상품 리스트를 반환
    public List<Product> cleanCart(CleanCartRequest request){
        List<Cart> carts = cartRepository.findAllByCartIdIn(request.getCartIds())
                .orElseThrow(() -> new IllegalArgumentException("not found cart"));

        //장바구니 리스트 -> 장바구니 아이디 리스트
        List<Long> productIds = carts.stream().mapToLong(cart -> cart.getProductId()).boxed().toList();
        //상품들중 품절 상품 검색
        List<Product> products = productService.findProductByProductIdInAndProductSold(productIds);

        //품절된 상품의 cartId를 추출
        List<Long> willDeleteCartIds = carts.stream().mapToLong(cart -> {
            boolean isSold = false;
            
            //cart 에 포함된 상품 아이디와 품절된 상품들 아이디를 대조
            for(Product product : products){
                if(product.getProductId() == cart.getProductId()){
                    isSold = true;
                    break;
                }
            }

            //모든 cartId 에 대해 품절 상품이 저장된 장바구니의 cartId 리턴 아닐경우 -1 리턴
            if(isSold){
                return cart.getCartId();
            }
            else{
                return -1;
            }
        })
        .boxed().toList();

        //품절 상품이 포함된 cartId만 추출
        willDeleteCartIds.forEach(cartId -> {
            if(cartId != -1){
                cartRepository.deleteById(cartId);
            }
        });

        return products;
    }

    //장바구니에 필요한 정보(CartViewResponse) 반환
    public List<CartViewResponse> findCartByPrincipal(Principal principal){
        List<Cart> carts = cartRepository.findByUserId(userService.getUserId(principal))
                .orElseThrow(() -> new IllegalArgumentException("not found cart"));

        List<CartViewResponse> list = new ArrayList<>();
        
        //cartId 리스트 -> CartViewResponse리스트
        carts.forEach(cart -> list.add(new CartViewResponse(cart.getCartId(), productService.findProductByProductId(cart.getProductId()))));
        return list;
    }
}
