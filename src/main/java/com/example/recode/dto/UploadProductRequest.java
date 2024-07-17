package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class UploadProductRequest {

    private Long productId;
    private String productName;
    private String productModel;
    private String productCategory;
    private int productRegularPrice;
    private Integer productDiscountPrice;
    private String productSize;
    private String productMaterial;
    private MultipartFile productRepImg;
    private String productColor;
    private String productType;
    private List<MultipartFile> productExtImg;

}