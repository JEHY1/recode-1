package com.example.recode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResetPasswordRequest {
    private Long userId;
    private String userPassword;
}
