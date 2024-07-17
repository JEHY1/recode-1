package com.example.recode.repository;

import com.example.recode.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {
    Optional<List<ProductImg>> findByProductId(long productId);
}
