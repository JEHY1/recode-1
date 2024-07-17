package com.example.recode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindPwRequest {

    private String username;
    private String userRealName;
    private String userEmail;
}
