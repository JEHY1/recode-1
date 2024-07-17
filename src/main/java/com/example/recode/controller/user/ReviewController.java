package com.example.recode.controller.user;
import com.example.recode.domain.Review;
import com.example.recode.dto.MyWrittenReview;
import com.example.recode.dto.ReviewDto;
import com.example.recode.dto.ReviewSubmitRequest;
import com.example.recode.dto.ReviewWithImagesResponse;
import com.example.recode.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @GetMapping("/reviews")
    public String getAllReviews(Model model, Pageable pageable) {
        // 페이지 크기를 설정하는 경우 (10개씩 출력)
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("reviewId").ascending());

        Page<ReviewWithImagesResponse> list = reviewService.getAllReviewWithImages(pageRequest);

        int firstPage = list.getNumber() + 1;
        int totalPages = list.getTotalPages();
        int startPage = Math.max(firstPage - 4, 1);
        int lastPage = Math.min(firstPage + 5, totalPages);

        model.addAttribute("reviews", list);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", firstPage);
        model.addAttribute("pageNumber", pageable.getPageNumber());

        return "/board/reviewList";
    }


    @GetMapping("/review/{reviewId}") //리뷰 상세보기
    public String getReviewById(@PathVariable Long reviewId, Model model) {
        ReviewWithImagesResponse reviewWithImagesResponse = reviewService.getReviewWithImages(reviewId);
        model.addAttribute("review", reviewWithImagesResponse.getReview());
        model.addAttribute("reviewImgs", reviewWithImagesResponse.getImgUrls());
        reviewService.updateViewCount(reviewId); // 조회수 증가 - 사용자 페이지에서 하기

        return "/board/reviewTxt";
    }

    @GetMapping("/reviewPost")
    public String showReviewForm(Model model) {
        model.addAttribute("reviewDto", new ReviewDto());
        model.addAttribute("productId", 2L); // 임시상품 아이디
        return "/board/reviewPost";
    }

    @PostMapping("/review/submit")
    public String postReview(@ModelAttribute ReviewSubmitRequest request, Model model, Principal principal) {
        reviewService.saveReview(request, principal);

        return "redirect:/reviews";
    }

    //마이페이지 리뷰
    @GetMapping("/myReviews")
    public String getAllMyReviews(Model model, Pageable pageable) {
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by("reviewId").ascending());

        Page<MyWrittenReview> list = reviewService.myWrittenReview(pageRequest);

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

    //마이페이지 리뷰리스트
    @GetMapping("/myReview/list")
    public String getWrittenReviews(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<MyWrittenReview> writtenReviews = reviewService.myWrittenReview(pageable);

        int firstPage = writtenReviews.getNumber() + 1;
        int totalPages = writtenReviews.getTotalPages();
        int startPage = Math.max(firstPage - 4, 1);
        int lastPage = Math.min(firstPage + 5, totalPages);
        System.err.println(writtenReviews.getContent());

        model.addAttribute("writtenReviews", writtenReviews);
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", firstPage);
        model.addAttribute("pageNumber", pageable.getPageNumber());

        return "/board/myReViewList";
    }
}


