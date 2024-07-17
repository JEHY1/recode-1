package com.example.recode.repository;

import com.example.recode.domain.Notice;
import com.example.recode.domain.Product;
import com.example.recode.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<List<Product>> findAllByProductIdInAndProductSold(List<Long> productIds, Integer productSold);
    Optional<List<Product>> findAllByProductIdIn(List<Long> productIds);
    @Query(value = "SELECT * FROM product_tb WHERE product_sold = 0 ORDER BY product_registration_date DESC LIMIT 8", nativeQuery = true)
    Optional<List<Product>> newProduct();
    @Query(value = "SELECT * FROM product_tb WHERE product_sold = 0 ORDER BY product_view_count DESC LIMIT 12", nativeQuery = true)
    Optional<List<Product>> bestProduct();
    Optional<Page<Product>> findAllByProductCategoryAndProductSold(String productCategory, int productSold, Pageable pageable);
    Optional<Page<Product>> findAllByProductNameContainingAndProductSold(String searchText, int productSold, Pageable pageable);
    Optional<List<Product>> findByProductNameContaining(String productName);
    Optional<Page<Product>> findByProductNameContaining(String searchKeyword, Pageable pageable); // productName 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductNameContainingAndProductSold(String searchKeyword, int group, Pageable pageable); // 판매여부로 분류하여 productName 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductModelContaining(String searchKeyword, Pageable pageable); // productModel 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductModelContainingAndProductSold(String searchKeyword, int group, Pageable pageable); // 판매여부로 분류하여 productModel 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegularPrice(int searchKeyword, Pageable pageable); // productRegularPrice 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductRegularPriceAndProductSold(int searchKeyword, int group, Pageable pageable); // 판매여부로 분류하여 productRegularPrice 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductDiscountPrice(int searchKeyword, Pageable pageable); // productDiscountPrice 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductDiscountPriceAndProductSold(int searchKeyword, int group, Pageable pageable); // 판매여부로 분류하여 productDiscountPrice 로 검색해서 페이징 처리한 Page<Product>
    Optional<Page<Product>> findByProductSold(int group, Pageable pageable); // productSold 로 검색해서 페이징 처리한 Page<Product>
    Optional<List<Product>> findByProductSold(int group); // productSold 로  List<Product> 조회
}
