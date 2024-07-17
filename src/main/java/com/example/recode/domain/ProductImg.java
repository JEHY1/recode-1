package com.example.recode.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_img_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id", updatable = false)
    private long productImgId;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "product_img_src", nullable = false)
    private String productImgSrc;

    @Builder
    public ProductImg(long productImgId, long productId, String productImgSrc) {
        this.productImgId = productImgId;
        this.productId = productId;
        this.productImgSrc = productImgSrc;
    }
}
