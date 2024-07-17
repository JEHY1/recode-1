package com.example.recode.dto;

import com.example.recode.domain.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartViewResponse {

    private long cartId;
    private long productId;
    private String productName;
    private int productRegularPrice;
    private Integer productDiscountPrice;
    private String productRepresentativeImgSrc;

    public CartViewResponse(long cartId, Product product){
        this.cartId = cartId;
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productRegularPrice = product.getProductRegularPrice();
        this.productDiscountPrice = product.getProductDiscountPrice();
        this.productRepresentativeImgSrc = product.getProductRepresentativeImgSrc();
    }
}
