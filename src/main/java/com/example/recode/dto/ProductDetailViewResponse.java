package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductDetailViewResponse {

    private long productId;
    private String productName;
    private String productModel;
    private String productRepImg;
    private List<String> productDetailImgs;
    private String color;
    private String type;
    private String size;
    private String material;
    private int regularPrice;
    private Integer discountPrice;
    private int productSold;


    @Builder
    public ProductDetailViewResponse(long productId ,String productName, String productModel, String productRepImg, List<String> productDetailImgs,
                                     String color, String type, String size, String material, int regularPrice, Integer discountPrice, int productSold) {
        this.productId = productId;
        this.productName = productName;
        this.productModel = productModel;
        this.productRepImg = productRepImg;
        this.productDetailImgs = productDetailImgs;
        this.color = color;
        this.type = type;
        this.size = size;
        this.material = material;
        this.regularPrice = regularPrice;
        this.discountPrice = discountPrice;
        this.productSold = productSold;
    }
}
