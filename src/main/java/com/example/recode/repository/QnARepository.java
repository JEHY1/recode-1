package com.example.recode.repository;

import com.example.recode.domain.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface QnARepository extends JpaRepository<QnA, Long> {

    Optional<List<QnA>> findByProductIdOrderByQnACreateDateDesc(long productId);
    Optional<Page<QnA>> findByProductIdIn(List<Long> productIds, Pageable pageable); // 전체문의에서 productId List 로 페이징 처리한 Page<QnA> 가져오기
    Optional<Page<QnA>> findByProductIdInAndQnAAnswerIsNull(List<Long> productIds, Pageable pageable); // 답변미완료에서 productId List 로 페이징 처리한 Page<QnA> 가져오기
    Optional<Page<QnA>> findByProductIdInAndQnAAnswerNotNull(List<Long> productIds, Pageable pageable); // 답변완료에서 productId List 로 페이징 처리한 Page<QnA> 가져오기
    Optional<Page<QnA>> findByQnAQuestionTitleContaining(String searchKeyword, Pageable pageable); // 전체문의에서 qnAQuestionTitle 로 검색해서 페이징 처리한 Page<QnA>
    Optional<Page<QnA>> findByQnAQuestionTitleContainingAndQnAAnswerIsNull(String searchKeyword, Pageable pageable); // 답변미완료에서 qnAQuestionTitle 로 검색해서 페이징 처리한 Page<QnA>
    Optional<Page<QnA>> findByQnAQuestionTitleContainingAndQnAAnswerNotNull(String searchKeyword, Pageable pageable); // 답변완료에서 qnAQuestionTitle 로 검색해서 페이징 처리한 Page<QnA>
    Optional<Page<QnA>> findByUserIdIn(List<Long> userIds, Pageable pageable);  // 전체문의에서 userId List 로 페이징 처리한 Page<QnA> 가져오기
    Optional<Page<QnA>> findByUserIdInAndQnAAnswerIsNull(List<Long> userIds, Pageable pageable);  // 답변미완료에서 userId List 로 페이징 처리한 Page<QnA> 가져오기
    Optional<Page<QnA>> findByUserIdInAndQnAAnswerNotNull(List<Long> userIds, Pageable pageable);  // 답변완료에서 userId List 로 페이징 처리한 Page<QnA> 가져오기
    Optional<Page<QnA>> findByQnAAnswerIsNull(Pageable pageable); // 답변미완료 상품문의 조회
    Optional<Page<QnA>> findByQnAAnswerNotNull(Pageable pageable); // 답변완료 상품문의 조회
    Optional<List<QnA>> findByQnAAnswerIsNull(); // 답변미완료 List<QnA> 가져오기
    Optional<List<QnA>> findByQnAAnswerNotNull(); // 답변미완료 List<QnA> 가져오기
    Optional<Page<QnA>> findByUserId(Pageable pageable, Long userId); // userId로 페이징처리한 Page<QnA> 가져오기

}




