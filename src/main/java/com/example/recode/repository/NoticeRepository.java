package com.example.recode.repository;

import com.example.recode.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Page<Notice>> findByNoticeTitleContaining(String searchKeyword, Pageable pageable); // noticeTitle 로 검색해서 페이징 처리한 Page<Notice>
    Optional<Page<Notice>> findByUserIdIn(List<Long> userIds, Pageable pageable); // userId List 로 페이징 처리한 Page<Notice> 가져오기
}
