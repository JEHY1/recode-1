package com.example.recode.repository;

import com.example.recode.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(long userId, long productId);
    Optional<List<Cart>> findByUserId(long userId);

    Optional<List<Cart>> findAllByCartIdIn(List<Long> cartIds);


}
