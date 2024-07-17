package com.example.recode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QnaAnswerRequest {

    private Long qnAId;
    private String qnAAnswer;

}
