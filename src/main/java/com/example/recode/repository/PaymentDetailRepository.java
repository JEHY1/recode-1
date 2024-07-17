package com.example.recode.repository;

import com.example.recode.domain.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

    Optional<List<PaymentDetail>> findByPaymentId(long paymentId);
    Optional<List<PaymentDetail>> findAllByPaymentIdIn(List<Long> paymentIds);
}
