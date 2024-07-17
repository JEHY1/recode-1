package com.example.recode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoticeRequest {

    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
}
