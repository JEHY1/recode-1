package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRealNameForm {

    private String frontText;
    private String searchText;
    private String endText;
    private String realName;

    @Builder
    public UserRealNameForm(String frontText, String searchText, String endText, String realName) {
        this.frontText = frontText;
        this.searchText = searchText;
        this.endText = endText;
        this.realName = realName;
    }
}
