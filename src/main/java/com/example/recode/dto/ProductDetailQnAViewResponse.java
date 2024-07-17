package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProductDetailQnAViewResponse {

    private int viewIndex;
    private long QnAId;
    private String QnAQuestionTitle;
    private String QnAQuestionContent;
    private String username;
    private String QnAAnswer;
    private LocalDateTime QnACreateDate;
    private LocalDateTime QnAAnswerDate;
    private int QnAViews;

    @Builder

    public ProductDetailQnAViewResponse(int viewIndex, long qnAId, String qnAQuestionTitle, String qnAQuestionContent, String username, String qnAAnswer,
                                        LocalDateTime qnACreateDate, LocalDateTime qnAAnswerDate, int qnAViews) {
        this.viewIndex = viewIndex;
        this.QnAId = qnAId;
        this.QnAQuestionTitle = qnAQuestionTitle;
        this.QnAQuestionContent = qnAQuestionContent;
        this.username = username;
        this.QnAAnswer = qnAAnswer;
        this.QnACreateDate = qnACreateDate;
        this.QnAAnswerDate = qnAAnswerDate;
        this.QnAViews = qnAViews;
    }
}
