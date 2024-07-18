package com.example.recode.controller.user;

import com.example.recode.domain.Notice;
import com.example.recode.dto.NoticeViewResponse;
import com.example.recode.dto.QnAViewResponse;
import com.example.recode.service.NoticeService;
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


@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;

    @GetMapping("/community/notice/list")
    public String getAllNotices(Model model, @PageableDefault(page = 0, size = 10, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<NoticeViewResponse> noticeList = noticeService.noticeViewList(pageable);
        model.addAttribute("notices", noticeList);

        // 페이징 관련 변수
        int nowPage = noticeList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = noticeList.getTotalPages() == 0 ? 1 : noticeList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "board/noticeList";
    }

    @GetMapping("/community/notice/{id}")
    public String getNoticeById(@PathVariable Long id, Model model) {
        Notice notice = noticeService.findById(id);
        model.addAttribute("notice", notice);
        noticeService.updateView(id);
        String username = userService.getUsername(notice.getUserId());
        model.addAttribute("username", username);
        System.err.println(username);

        return "board/noticeTxt";
    }
}