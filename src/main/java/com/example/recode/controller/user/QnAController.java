package com.example.recode.controller.user;

import com.example.recode.domain.QnA;
import com.example.recode.domain.Review;
import com.example.recode.domain.User;
import com.example.recode.dto.QnAPhotoUserViewResponse;
import com.example.recode.dto.QnAPhotoViewResponse;
import com.example.recode.dto.QnAViewResponse;
import com.example.recode.service.QnAService;
import com.example.recode.service.UserService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnAService;
    private final UserService userService;

    @GetMapping("/community/qna/list")
    public String getAllQnAs(Model model, @PageableDefault(page = 0, size = 10, sort = "qnAId", direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {

        Page<QnAPhotoViewResponse> qnAList = qnAService.qnAPhotoViewList(pageable);
        model.addAttribute("QnAs", qnAList);

        // 페이징 관련 변수
        int nowPage = qnAList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = qnAList.getTotalPages() == 0 ? 1 : qnAList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("userId", principal != null ? userService.getUserId(principal) : null);


        return "/board/queryList";
    }

    @GetMapping("/community/qna/{id}")
    public String getQnaById(@PathVariable Long id, Model model, Principal principal) {
        QnA qna = qnAService.getQnaInfo(id, principal);
        if(qna == null){

            return "/error/403";
        }

        model.addAttribute("qna", qna);



        String username = userService.getUsername(qna.getUserId());
        model.addAttribute("username", username);

        return "/board/queryTxt";
    }

    @GetMapping("/user/qna/list")
    public String getUserQnAs(Model model, @PageableDefault(page = 0, size = 10, sort = "qnAId", direction = Sort.Direction.DESC) Pageable pageable, Principal principal) {

        Page<QnAPhotoUserViewResponse> qnAList = qnAService.qnAPhotoUserViewList(pageable, principal);
        model.addAttribute("QnAs", qnAList);

        // 페이징 관련 변수
        int nowPage = qnAList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = qnAList.getTotalPages() == 0 ? 1 : qnAList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "/board/myQueryList";
    }

    @GetMapping("/user/qna/{id}")
    public String getUserQnaById(@PathVariable Long id, Model model) {
        QnA qna = qnAService.findById(id);
        model.addAttribute("qna", qna);

        return "/board/myQueryTxt";
    }


}
