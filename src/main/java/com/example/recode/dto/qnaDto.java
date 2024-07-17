package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class qnaDto {
    private Long QnAId;
    private long userId;
    private String username;
    private String productName;
    private String QnAQuestionTitle;
    private String QnAAnswer;
    private LocalDateTime QnACreateDate;
    private int QnAViews;

}
