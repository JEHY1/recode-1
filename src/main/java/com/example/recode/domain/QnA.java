package com.example.recode.domain;

import com.example.recode.dto.QnaAnswerRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "qna_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id", updatable = false)
    private Long qnAId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "qna_question_title", nullable = false)
    private String qnAQuestionTitle;

    @Column(name = "qna_question_content", nullable = false)
    private String qnAQuestionContent;

    @Column(name = "qna_answer")
    private String qnAAnswer;

    @CreatedDate
    @Column(name = "qna_create_date", nullable = false, updatable = false)
    private LocalDateTime qnACreateDate;

    @Column(name = "qna_views", nullable = false)
    private int qnAViews;

    @Column(name = "qna_answer_date")
    private LocalDateTime qnAAnswerDate;

    @Column(name = "qna_secret", nullable = false)
    private int qnASecret;

    @Builder
    public QnA(Long qnAId, long userId, long productId, String qnAQuestionTitle, String qnAQuestionContent, String qnAAnswer, LocalDateTime qnACreateDate, int qnAViews, LocalDateTime qnAAnswerDate, int qnASecret) {
        this.qnAId = qnAId;
        this.userId = userId;
        this.productId = productId;
        this.qnAQuestionTitle = qnAQuestionTitle;
        this.qnAQuestionContent = qnAQuestionContent;
        this.qnAAnswer = qnAAnswer;
        this.qnACreateDate = qnACreateDate;
        this.qnAViews = qnAViews;
        this.qnAAnswerDate = qnAAnswerDate;
        this.qnASecret = qnASecret;
    }

    public QnA saveAnswer(QnaAnswerRequest dto) { // 상품문의 답변 등록&수정
        this.qnAAnswer = dto.getQnAAnswer();
        this.qnAAnswerDate = this.qnAAnswerDate == null ? LocalDateTime.now() : this.qnAAnswerDate;
        return this;
    }

    public QnA deleteAnswer() { // 상품문의 답변 지우기
        this.qnAAnswer = null;
        this.qnAAnswerDate = null;
        return this;
    }

    // 0715 조회수 증가 메서드 추가
    public QnA updateViews() {
        this.qnAViews++;
        return this;
    }
}
