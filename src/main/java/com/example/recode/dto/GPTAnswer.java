package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GPTAnswer {

    private String message;
    private String answer;

    @Builder
    public GPTAnswer(String message, String answer) {
        this.message = message;
        this.answer = answer;
    }
}
