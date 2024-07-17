package com.example.recode.dto;

import com.example.recode.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NoticeViewResponse {

    private Long noticeId;
    private String username;
    private String noticeTitle;
    private LocalDateTime noticeCreateDate;
    private int noticeViews;

    public NoticeViewResponse(Notice notice, String username) {
        this.noticeId = notice.getNoticeId();
        this.username = username;
        this.noticeTitle = notice.getNoticeTitle();
        this.noticeCreateDate = notice.getNoticeCreateDate();
        this.noticeViews = notice.getNoticeViews();
    }
}
