package com.example.recode.repository;

import com.example.recode.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<List<Payment>> findByUserId(long userId);
    Optional<Page<Payment>> findByUserIdAndPaymentDateBetweenOrderByPaymentDateDesc(long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Optional<List<Payment>> findByUserIdAndPaymentDateBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate >= :startDate AND p.paymentDate <= :endDate ORDER BY p.paymentDate DESC")
    Optional<List<Payment>> findPaymentsInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}
