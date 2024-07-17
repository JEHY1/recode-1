package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaSubmitRequest {

    private long productId;
    private String qnaTitle;
    private String qnaContent;
}
