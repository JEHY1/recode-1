package com.example.recode.controller.user;

import com.example.recode.domain.Notice;
import com.example.recode.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/notices")
    public String getAllNotices(Model model, Pageable pageable) {
        // 명시적으로 페이지 크기를 설정하는 경우 (10개씩 출력)
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("noticeId").ascending());

        Page<Notice> list = noticeService.getAllNotices(pageRequest);

        int firstPage = list.getNumber() + 1;
        int totalPages = list.getTotalPages();
        int startPage = Math.max(firstPage - 4, 1);
        int lastPage = Math.min(firstPage + 5, totalPages);

        model.addAttribute("list", list);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", firstPage);
        model.addAttribute("pageNumber", pageable.getPageNumber());

        return "/board/noticeList";
    }

    @GetMapping("/notice/{id}")
    public String getNoticeById(@PathVariable Long id, Model model) {
        Notice notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        noticeService.updateView(id);

        return "board/noticeTxt";
    }
}