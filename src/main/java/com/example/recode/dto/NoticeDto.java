package com.example.recode.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter
public class NoticeDto {
    private Long noticeId;
    private String username;
    private LocalDateTime noticeCreateDate;
    private String noticeTitle;
    private String noticeContent;
    private int noticeViews;
}
