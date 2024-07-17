package com.example.recode.domain;

import com.example.recode.dto.NoticeRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Notice {

    @Id
    @Column(name = "notice_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "notice_title", nullable = false)
    private String noticeTitle;

    @Column(name = "notice_content")
    private String noticeContent;

    @CreatedDate
    @Column(name = "notice_create_date", nullable = false, updatable = false)
    private LocalDateTime noticeCreateDate;

    @Column(name = "notice_views", nullable = false)
    private int noticeViews;

    @Builder
    public Notice(Long noticeId, long userId, String noticeTitle, String noticeContent, LocalDateTime noticeCreateDate, int noticeViews) {
        this.noticeId = noticeId;
        this.userId = userId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCreateDate = noticeCreateDate;
        this.noticeViews = noticeViews;
    }

    public Notice update(NoticeRequest dto) { //공지사항 수정
        this.noticeTitle = dto.getNoticeTitle();
        this.noticeContent = dto.getNoticeContent();
        return this;
    }

    public Notice updateViews(){ //조회수 증가
        this.noticeViews++;
        return this;
    }


}
