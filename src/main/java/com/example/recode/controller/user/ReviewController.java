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
    public String showReviewForm(@RequestParam long paymentDetailId, long productId, Model model) {
        model.addAttribute("reviewDto", new ReviewDto());
        model.addAttribute("productId", productId);
        model.addAttribute("paymentDetailId", paymentDetailId);
        return "/board/reviewPost";
    }

    @PostMapping("/review/submit")
    public String postReview(@ModelAttribute ReviewSubmitRequest request, Model model, Principal principal) {
        reviewService.saveReview(request, principal);

        return "redirect:/myReviews?tap=1";
    }

    //마이페이지 리뷰(작성가능한 항목)
    @GetMapping("/myReviews")
    public String getAllMyReviews(Model model, @RequestParam(defaultValue = "0") int page1, @RequestParam(defaultValue = "0") int page2, @RequestParam(required = false, defaultValue = "0") int tap,Principal principal) {

        Pageable pageable1 = PageRequest.of(page1, 10);
        Pageable pageable2 = PageRequest.of(page2, 10);

        System.err.println(reviewService.myWrittenReview(pageable2, principal));

        model.addAttribute("writableReviewPage", reviewService.myWritableReview(pageable1, principal));
        model.addAttribute("writtenReviewPage", reviewService.myWrittenReview(pageable2, principal));


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

}


