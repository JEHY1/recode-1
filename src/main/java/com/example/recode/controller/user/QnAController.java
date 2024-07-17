package com.example.recode.controller.user;

import com.example.recode.domain.QnA;
import com.example.recode.domain.Review;
import com.example.recode.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnaService;

    @GetMapping("/qnaList")
    public String getAllQnAs(Model model, Pageable pageable) {
        // 명시적으로 페이지 크기를 설정하는 경우 (10개씩 출력)
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("QnAId").ascending());

        Page<QnA> list = qnaService.getAllQnAs(pageRequest);

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

        return "/board/queryList";
    }

    @GetMapping("/qna/{id}")
    public String getQnaById(@PathVariable Long id, Model model) {
        QnA qna = qnaService.findById(id);
        model.addAttribute("qna", qna);
//        0715 updateView 메서드를 호출하여 조회수를 증가시키도록 수정
//        조회수 증가
        qnaService.updateView(id);
        return "/board/queryTxt";
    }


}
