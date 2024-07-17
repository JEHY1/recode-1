package com.example.recode.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Cart {

    @Id
    @Column(name = "cart_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Builder
    public Cart(long cartId, long userId, long productId) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
    }
}
