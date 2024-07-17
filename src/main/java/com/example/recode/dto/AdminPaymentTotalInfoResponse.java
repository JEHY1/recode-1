package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminPaymentTotalInfoResponse {

    private List<List<AdminPaymentInfoResponse>> paymentInfo;
    private int totalDeposit;
    private int totalPaymentCompleteDeposit;
    private int totalPaymentNonDeposit;
    private int totalPaymentCancelDeposit;

    private int totalDepositCount;
    private int totalPaymentCompleteDepositCount;
    private int totalPaymentNonDepositCount;
    private int totalPaymentCancelDepositCount;

    private int totalPaymentCount;
    private int totalPaymentNonCount;
    private int totalPaymentCompleteCount;
    private int totalPaymentCancelCount;

    @Builder
    public AdminPaymentTotalInfoResponse(List<List<AdminPaymentInfoResponse>> paymentInfo, int totalDeposit, int totalPaymentCompleteDeposit, int totalPaymentNonDeposit, int totalPaymentCancelDeposit, int totalDepositCount, int totalPaymentCompleteDepositCount, int totalPaymentNonDepositCount, int totalPaymentCancelDepositCount, int totalPaymentCount, int totalPaymentNonCount, int totalPaymentCompleteCount, int totalPaymentCancelCount) {
        this.paymentInfo = paymentInfo;
        this.totalDeposit = totalDeposit;
        this.totalPaymentCompleteDeposit = totalPaymentCompleteDeposit;
        this.totalPaymentNonDeposit = totalPaymentNonDeposit;
        this.totalPaymentCancelDeposit = totalPaymentCancelDeposit;
        this.totalDepositCount = totalDepositCount;
        this.totalPaymentCompleteDepositCount = totalPaymentCompleteDepositCount;
        this.totalPaymentNonDepositCount = totalPaymentNonDepositCount;
        this.totalPaymentCancelDepositCount = totalPaymentCancelDepositCount;
        this.totalPaymentCount = totalPaymentCount;
        this.totalPaymentNonCount = totalPaymentNonCount;
        this.totalPaymentCompleteCount = totalPaymentCompleteCount;
        this.totalPaymentCancelCount = totalPaymentCancelCount;
    }
}
