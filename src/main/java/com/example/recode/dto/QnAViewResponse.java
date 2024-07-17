package com.example.recode.dto;

import com.example.recode.domain.QnA;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class QnAViewResponse {

    private Long qnAId;
    private long userId;
    private String username;
    private String productName;
    private String qnAQuestionTitle;
    private String qnAAnswer;
    private LocalDateTime qnACreateDate;
    private int qnAViews;

    public QnAViewResponse(QnA qnA, String username, String productName) {
        this.qnAId = qnA.getQnAId();
        this.userId = qnA.getUserId();
        this.username = username;
        this.productName = productName;
        this.qnAQuestionTitle = qnA.getQnAQuestionTitle();
        this.qnAAnswer = qnA.getQnAAnswer();
        this.qnACreateDate = qnA.getQnACreateDate();
        this.qnAViews = qnA.getQnAViews();
    }
}
