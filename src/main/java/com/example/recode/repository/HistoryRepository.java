package com.example.recode.repository;

import com.example.recode.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    Optional<List<History>> findAllByUserId(long userId);
    Optional<History> findByProductIdAndUserId(long productId, long userId);
    Optional<List<History>> findByProductId(long productId);
}
