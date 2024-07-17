package com.example.recode.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", updatable = false)
    private long paymentId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "payment_price", nullable = false)
    private int paymentPrice;

    @CreatedDate
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "payment_bank")
    private String paymentBank;

    @Column(name = "payment_account_number")
    private String paymentAccountNumber;

    @Column(name = "payment_card")
    private String paymentCard;

    @Column(name = "payment_card_number")
    private String paymentCardNumber;

    @Column(name = "payment_installment")
    private Integer paymentInstallment;

    @Column(name = "payment_phone_company")
    private String paymentPhoneCompany;

    @Column(name = "payment_micropayment_phone")
    private String paymentMicropaymentPhone;

    @Column(name = "payment_depositor")
    private String paymentDepositor;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_postal_code", nullable = false)
    private String paymentPostalCode;

    @Column(name = "payment_address", nullable = false)
    private String paymentAddress;

    @Column(name = "payment_delivery_request", nullable = false)
    private String paymentDeliveryRequest;

    @Column(name = "payment_recipient_name", nullable = false)
    private String paymentRecipientName;

    @Column(name = "payment_recipient_phone", nullable = false)
    private String paymentRecipientPhone;

    @Column(name = "payment_delivery_fee", nullable = false)
    private int paymentDeliveryFee;

    @Column(name = "payment_front_door_secret")
    private String paymentFrontDoorSecret;

    @Column(name = "payment_delivery_box_num")
    private String paymentDeliveryBoxNum;

    @Builder
    public Payment(long paymentId, long userId, int paymentPrice, LocalDateTime paymentDate, String paymentType, String paymentBank, String paymentAccountNumber, String paymentCard, String paymentCardNumber, Integer paymentInstallment, String paymentPhoneCompany, String paymentMicropaymentPhone, String paymentDepositor, String paymentStatus, String paymentPostalCode, String paymentAddress, String paymentDeliveryRequest, String paymentRecipientName, String paymentRecipientPhone, int paymentDeliveryFee, String paymentFrontDoorSecret, String paymentDeliveryBoxNum) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentPrice = paymentPrice;
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentBank = paymentBank;
        this.paymentAccountNumber = paymentAccountNumber;
        this.paymentCard = paymentCard;
        this.paymentCardNumber = paymentCardNumber;
        this.paymentInstallment = paymentInstallment;
        this.paymentPhoneCompany = paymentPhoneCompany;
        this.paymentMicropaymentPhone = paymentMicropaymentPhone;
        this.paymentDepositor = paymentDepositor;
        this.paymentStatus = paymentStatus;
        this.paymentPostalCode = paymentPostalCode;
        this.paymentAddress = paymentAddress;
        this.paymentDeliveryRequest = paymentDeliveryRequest;
        this.paymentRecipientName = paymentRecipientName;
        this.paymentRecipientPhone = paymentRecipientPhone;
        this.paymentDeliveryFee = paymentDeliveryFee;
        this.paymentFrontDoorSecret = paymentFrontDoorSecret;
        this.paymentDeliveryBoxNum = paymentDeliveryBoxNum;
    }

    public void updatePaymentStatus(String paymentStatus){
        this.paymentStatus = paymentStatus;
    }
}
