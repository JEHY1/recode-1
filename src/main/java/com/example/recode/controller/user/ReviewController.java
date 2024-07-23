package com.example.recode.controller.user;
import com.example.recode.domain.Review;
import com.example.recode.domain.ReviewImg;
import com.example.recode.dto.*;
import com.example.recode.service.ReviewService;
import com.example.recode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/community/review/list")
    public String getAllReviews(Model model, @PageableDefault(page = 0, size = 10, sort = "reviewId", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewPhotoResponse> reviewList = reviewService.reviewPhotoViewList(pageable);
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

        return "/board/reviewList";
    }


    @GetMapping("/community/review/{reviewId}") //리뷰 상세보기
    public String getReviewById(@PathVariable Long reviewId, Model model) {

        Review reviewEntity = reviewService.getReviewFindById(reviewId);
        model.addAttribute("review", reviewEntity);
        List<ReviewImg> reviewImgList = reviewService.getReviewImgFindByReviewId(reviewEntity.getReviewId());
        model.addAttribute("reviewImgs", reviewImgList);
        String username = userService.getUsername(reviewEntity.getUserId());
        model.addAttribute("username", username);


        reviewService.updateViewCount(reviewId); // 조회수 증가 - 사용자 페이지에서 하기

        return "/board/reviewTxt";
    }

    @GetMapping("/reviewPost")
    public String showReviewForm(@RequestParam(required = false) Long paymentDetailId, @RequestParam(required = false) Long productId, @RequestParam(required = false) Long reviewId, Model model) {
        model.addAttribute("review", reviewService.getMyReviewInfo(reviewId, productId, paymentDetailId));

        return "/board/reviewPost";
    }

    @PostMapping("/review/submit")
    public String postReview(@ModelAttribute ReviewSubmitRequest request, Model model, Principal principal) {
        reviewService.saveReview(request, principal);

        return "redirect:/myReviews?tab=1";
    }

    //마이페이지 리뷰(작성가능한 항목)
    @GetMapping("/myReviews")
    public String getAllMyReviews(Model model, @RequestParam(defaultValue = "0") int page1, @RequestParam(defaultValue = "0") int page2, @RequestParam(required = false, defaultValue = "0") int tab, @RequestParam(required = false) Integer isDel, Principal principal) {

        Pageable pageable1 = PageRequest.of(page1, 10);
        Pageable pageable2 = PageRequest.of(page2, 10);

        System.err.println(reviewService.myWrittenReview(pageable2, principal));

        model.addAttribute("writableReviewPage", reviewService.myWritableReview(pageable1, principal));
        model.addAttribute("writtenReviewPage", reviewService.myWrittenReview(pageable2, principal));

        if(isDel != null && isDel == 0) { // 체크 안하고 삭제 눌렀을 때 팝업창
            model.addAttribute("msg", "삭제할 리뷰를 선택해주세요.");
        }

        return "/board/myReviews";
    }

    //마이페이지 리뷰상세
    @GetMapping("/myReview/{reviewId}")
    public String getMyReviewById(@PathVariable Long reviewId, Model model) {
        Review myReview = reviewService.findById(reviewId);
        model.addAttribute("myReview", myReview);

        // 조회수 증가
        reviewService.updateViewCount(reviewId);

        return "board/myReviewTxt";
    }

    // 리뷰 삭제
    @GetMapping("/myReview/{reviewId}/delete")
    public String deleteMyReviewById(@PathVariable Long reviewId, RedirectAttributes rttr) {
        reviewService.deleteById(reviewId);
        rttr.addFlashAttribute("msg", "리뷰가 삭제 되었습니다.");
        return "redirect:/myReviews";
    }

    @PostMapping("/myReview/delete")
    @ResponseBody
    public String deleteMyReviewSelect(@RequestParam(required = false) List<Long> reviewIds) {
        if(reviewIds != null) {
            reviewService.deleteByIds(reviewIds);
            return "delete";
        }
        else {
            return "null";
        }
    }

}


