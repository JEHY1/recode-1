package com.example.recode.controller.admin;

import com.example.recode.domain.Product;
import com.example.recode.domain.Review;
import com.example.recode.domain.ReviewImg;
import com.example.recode.dto.NoticeViewResponse;
import com.example.recode.dto.ReviewResponse;
import com.example.recode.service.ProductService;
import com.example.recode.service.ReviewService;
import com.example.recode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/admin/review/{reviewId}/show") // admin_review_show 리뷰 보기 페이지
    public String showAdminReview(@PathVariable Long reviewId, Model model) {
        Review reviewEntity = reviewService.getReviewFindById(reviewId);
        model.addAttribute("review", reviewEntity);
        List<ReviewImg> reviewImgList = reviewService.getReviewImgFindByReviewId(reviewEntity.getReviewId());
        model.addAttribute("reviewImgs", reviewImgList);
        String username = userService.getUsername(reviewEntity.getUserId());
        model.addAttribute("username", username);
        Product productEntity = productService.findProductByProductId(reviewEntity.getProductId());
        model.addAttribute("product", productEntity);

        return "admins/admin_review_show";
    }

    @GetMapping("/admin/review/{reviewId}/delete") // 리뷰 삭제
    public String deleteAdminReview(@PathVariable Long reviewId, RedirectAttributes rttr, Integer page, Integer category, String searchKeyword) throws UnsupportedEncodingException {
        reviewService.deleteById(reviewId);
        rttr.addFlashAttribute("msg", "리뷰가 삭제 되었습니다.");
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8); // ASCII 아닌 파라미터 percent encoding
        return "redirect:/admin/review/index?page=" + page + "&category=" + category + "&searchKeyword=" + encodeSearchKeyword;
    }

    @GetMapping("/admin/review/index") // admin_review_index 리뷰 목록 페이지
    public String indexAdminReview(Model model, @PageableDefault(page = 0, size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "0") Integer category, @RequestParam(defaultValue = "") String searchKeyword) {
        // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
        model.addAttribute("category", category);
        model.addAttribute("searchKeyword", searchKeyword);

        Page<ReviewResponse> reviewList = null;
        if(searchKeyword == null || searchKeyword.equals("")) { // 검색 안했을 때
            reviewList = reviewService.reviewViewList(pageable);
        }
        else { // searchKeyword 로 검색 됐을 때
            reviewList = reviewService.reviewViewSearchList(category, searchKeyword, pageable);
        }
        model.addAttribute("reviews", reviewList);

        // 페이징 관련 변수
        int nowPage = reviewList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = reviewList.getTotalPages() == 0 ? 1 : reviewList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        if(isDel != null && isDel == 1) { // 체크해서 삭제 했을 때 팝업창
            model.addAttribute("msg", "리뷰가 삭제 되었습니다.");
        }
        if(isDel != null && isDel == 0) { // 체크 안하고 삭제 눌렀을 때 팝업창
            model.addAttribute("msg", "삭제할 리뷰를 선택해주세요.");
        }

        return "admins/admin_review_index";
    }


    @PostMapping("/admin/review/delete")
    @ResponseBody
    public String deleteAdminReviewSelect(@RequestParam(required = false) List<Long> reviewIds) {
        if(reviewIds != null) {
            reviewService.deleteByIds(reviewIds);
            return "delete";
        }
        else {
            return "null";
        }
    }
}
