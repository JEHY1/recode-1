package com.example.recode.dto;

import com.example.recode.domain.Product;
import com.example.recode.domain.QnA;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class QnAPhotoUserViewResponse {

    private Long qnAId;
    private String productName;
    private String productRepresentativeImgSrc;
    private String qnAQuestionTitle;
    private String qnAAnswer;
    private LocalDateTime qnACreateDate;
    private int qnAViews;
    private int qnASecret;

    public QnAPhotoUserViewResponse(QnA qnA, Product product) {
        this.qnAId = qnA.getQnAId();
        this.productName = product.getProductName();
        this.productRepresentativeImgSrc = product.getProductRepresentativeImgSrc();
        this.qnAQuestionTitle = qnA.getQnAQuestionTitle();
        this.qnAAnswer = qnA.getQnAAnswer();
        this.qnACreateDate = qnA.getQnACreateDate();
        this.qnAViews = qnA.getQnAViews();
        this.qnASecret = qnA.getQnASecret();
    }
}