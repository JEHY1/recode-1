package com.example.recode.service;

import com.example.recode.domain.*;
import com.example.recode.dto.*;
import com.example.recode.repository.ReviewImgRepository;
import com.example.recode.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    //application.properties 의 app.upload.dir 값을 사용
    @Value("${app.upload.dir}")
    private String uploadDir;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final UserService userService;
    private final ProductService productService;
    private final PaymentService paymentService;

    public Review getReviewFindById(Long reviewId) { // reviewId로 Review 가져오기
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found review"));
    }

    public List<Review> getReviewFindAll() { // List<Review> 가져오기
        return reviewRepository.findAll();
    }

    public List<ReviewImg> getReviewImgFindByReviewId(long reviewId) { // reviewId로 List<ReviewImg> 가져오기
        return reviewImgRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found reviewimgs"));
    }

    public void deleteById(Long reviewId) { // reviewId로 Review 삭제
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewResponse> getAllReviewInfo() { // List<ReviewResponse> 가져오기

        List<ReviewResponse> list = new ArrayList<>();

        getReviewFindAll().forEach(review -> list.add(new ReviewResponse(review, userService.getUsername(review.getUserId()), productService.findProductByProductId(review.getProductId()).getProductName())));

        return list;
    }

    public Page<ReviewResponse> reviewViewList(Pageable pageable) { // 페이징 처리한 Page<ReviewResponse> 가져옴
        Page<Review> reviewList = reviewRepository.findAll(pageable); // 페이징 처리한 Page<Review>
        Page<ReviewResponse> reviewViewList = reviewList.map(review -> new ReviewResponse(review, userService.getUsername(review.getUserId()), productService.findProductByProductId(review.getProductId()).getProductName()));
        return reviewViewList;
    }

    public Page<ReviewResponse> reviewViewSearchList(Integer category, String searchKeyword, Pageable pageable) { // category 선택 후 검색해서 페이징 처리한 Page<ReviewResponse> 가져옴
        Page<ReviewResponse> reviewViewSearchList = null;
        if(category == 1) {  // category 가 '상품명'일 때
            List<Product> productSearchList = productService.findByProductNameContaining(searchKeyword);
            List<Long> productIds = new ArrayList<>();
            if(productSearchList != null) {
                productSearchList.forEach(product -> productIds.add(product.getProductId()));
            }
            Page<Review> ReviewSearchList = reviewRepository.findByProductIdIn(productIds, pageable).orElse(null); // productId List 로 페이징 처리한 Page<Review> 가져오기
            if(ReviewSearchList != null) {
                reviewViewSearchList = ReviewSearchList.map(review -> new ReviewResponse(review, userService.getUsername(review.getUserId()), productService.findProductByProductId(review.getProductId()).getProductName()));
            }
        }
        else if(category == 2) {  // category 가 '제목'일 때
            Page<Review> ReviewSearchList = reviewRepository.findByReviewTitleContaining(searchKeyword, pageable).orElse(null); // reviewTitle 로 검색해서 페이징 처리한 Page<Review>
            if(ReviewSearchList != null) {
                reviewViewSearchList = ReviewSearchList.map(review -> new ReviewResponse(review, userService.getUsername(review.getUserId()), productService.findProductByProductId(review.getProductId()).getProductName()));
            }
        }
        else if(category == 3) {  // category 가 '작성자'일 때
            List<User> userSearchList = userService.findUserByUsernameContaining(searchKeyword);
            List<Long> userIds = new ArrayList<>();
            if(userSearchList != null) {
                userSearchList.forEach(user -> userIds.add(user.getUserId()));
            }
            Page<Review> ReviewSearchList = reviewRepository.findByUserIdIn(userIds, pageable).orElse(null); // userId List 로 페이징 처리한 Page<Review> 가져오기
            if(ReviewSearchList != null) {
                reviewViewSearchList = ReviewSearchList.map(review -> new ReviewResponse(review, userService.getUsername(review.getUserId()), productService.findProductByProductId(review.getProductId()).getProductName()));
            }
        }
        else if(category == 4) { // category 가 '등록일'일 때
            // searchKeyword 가 포함된 List<ReviewResponse> 만듬
            List<ReviewResponse> keywordList = new ArrayList<>();
            getAllReviewInfo().forEach(review -> {
                if(review.getReviewCreateDate().toString().contains(searchKeyword)){
                    keywordList.add(review);
                }
            });
            keywordList.sort((o1, o2) -> Math.toIntExact(o2.getReviewId() - o1.getReviewId())); // reviewId 기준 내림차순 정렬
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), keywordList.size());
            reviewViewSearchList = new PageImpl<>(keywordList.subList(start, end), pageable, keywordList.size()); // Page<reviewViewSearchList> 객체 만듬
        }
        return reviewViewSearchList;
    }

    public void deleteByIds(List<Long> reviewIds) { // reviewIds 리스트로 Review 삭제
        for (Long reviewId : reviewIds) {
            reviewRepository.deleteById(reviewId);
        }
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    @Transactional
    public void saveReview(ReviewSubmitRequest request, Principal principal) {
        Review review = null;
        if(request.getReviewId() != null) {
            review = findById(request.getReviewId()).updateReview(request); // 리뷰 수정
        }
        else {
            review = Review.builder()
                    .userId(userService.getUserId(principal))
                    .productId(request.getProductId())
                    .paymentDetailId(request.getPaymentDetailId())
                    .reviewTitle(request.getReviewTitle())
                    .reviewContent(request.getReviewContent())
                    .reviewScore(request.getReviewScore())
                    .reviewViews(0)
                    .build();

            reviewRepository.save(review);
        }

        // files 등록할 경우 저장
        if(!request.getFiles().get(0).isEmpty()) {
            // 상품정보 수정 시 새로 업로드 하는 파일이 있으면 DB 에서 기존 productImg 삭제
            if(request.getReviewId() != null) {
                System.err.println(getReviewImgFindByReviewId(request.getReviewId()));
                getReviewImgFindByReviewId(request.getReviewId()).forEach(reviewImg -> reviewImgRepository.deleteById(reviewImg.getReviewImgId()));
            }

            int index = 1;
            for (MultipartFile file : request.getFiles()) {

                String originalFileName = file.getOriginalFilename();
                //파일 확장자 추출
                int extensionIndex = originalFileName.lastIndexOf(".");
                String extension = originalFileName.substring(extensionIndex);

                ReviewImg reviewImg = ReviewImg.builder()
                        .reviewId(review.getReviewId())
                        .reviewImgSrc("/images/reviewImg/review" + review.getReviewId() + "_" + index + extension)
                        .build();
                reviewImgRepository.save(reviewImg);

                //파일이름 추출
                fileUpload(file, review.getReviewId(), extension, "reviewImg", index++);
            }
        }


    }

    //파일 업로드(파일, 상품 아이디, 파일 확장자, 추가 경로, 파일 번호)
    public void fileUpload(MultipartFile multipartFile, long reviewId, String extension, String dir, Integer num) {

        //경로 만들기
        Path copyOfLocation = Paths.get(uploadDir + File.separator + dir + File.separator + "review" + reviewId + "_" + (num == null ? "" : num) + extension);

        System.err.println(copyOfLocation.toString());
        try {
            // inputStream 사용
            // copyOfLocation 저장위치
            // 기존 파일이 존재할 경우 덮어쓰기
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not store file : " + multipartFile.getOriginalFilename());
        }
    }

    public MyWrittenReviewResponse myWrittenReview(Pageable pageable, Principal principal){
        System.err.println("callMyWrittenReview");
        Page<Review> reviews = reviewRepository.findAllByUserId(userService.getUserId(principal), pageable)
                .orElseThrow(() -> new IllegalArgumentException("not found review"));

        reviews.getTotalPages();

        List<MyWrittenReview> reviewList = new ArrayList<>();
        Page<MyWrittenReview> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());
        if(!reviews.isEmpty()){
            reviewList = reviews.getContent().stream().map(this::toMyWrittenReview).collect(Collectors.toList());
            reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());
        }


        return MyWrittenReviewResponse.builder()
                .myWrittenReviewList(reviewPage)
                .totalPages(reviews.getTotalPages())
                .build();
    }

    public MyWrittenReview toMyWrittenReview(Review review){
        List<ReviewImg> reviewImgs = getReviewImgFindByReviewId(review.getReviewId());
        Product product = productService.findProductByProductId(review.getProductId());

        return MyWrittenReview.builder()
                .review(review)
                .productName(product.getProductName())
                .reviewRepImg(!reviewImgs.isEmpty() ? reviewImgs.get(0).getReviewImgSrc() : null)
                .build();
    }

    @Transactional
    public Review updateViewCount(long reviewId){
        return findById(reviewId).updateViews();
    }

    public MyWritableReviewResponse myWritableReview(Pageable pageable, Principal principal) {
        List<Long> paymentIdList = paymentService.findPaymentByPrincipal(principal).stream().map(Payment::getPaymentId).collect(Collectors.toList());
        List<PaymentDetail> paymentDetailList = paymentService.findPaymentDetailByPaymentIdIn(paymentIdList);

        List<Review> reviews = reviewRepository.findAllByUserId(userService.getUserId(principal))
                .orElseThrow(() -> new IllegalArgumentException("not found review"));

        List<Long> writtenReviewPaymentDetailIdList = reviews.stream().map(Review::getPaymentDetailId).collect(Collectors.toList());

        List<PaymentDetail> paymentDetailList2 = new ArrayList<>();
        paymentDetailList.forEach(paymentDetail -> {
            boolean isInclude = false;
            for(Review review : reviews){
                if(review.getProductId() == paymentDetail.getProductId()){
                    isInclude = true;
                    break;
                }
            }
            if(!isInclude && paymentDetail.getPaymentDetailStatus().equals("배송완료")){
                paymentDetailList2.add(paymentDetail);
            }
        });


        int totalPage = paymentDetailList2.size() / pageable.getPageSize() + 1;
        System.err.println("start : " + pageable.getPageNumber() * pageable.getPageSize());
        System.err.println("end : " + Math.min(pageable.getPageNumber() * pageable.getPageSize() + pageable.getPageSize(), paymentDetailList2.size()));
        List<PaymentDetail> subList = paymentDetailList2.subList(pageable.getPageNumber() * pageable.getPageSize(), Math.min(pageable.getPageNumber() * pageable.getPageSize() + pageable.getPageSize(), paymentDetailList2.size()));
        List<MyWritableReview> subMyWriteableReviewList = subList.stream().map(this::toMyWriteableReview).collect(Collectors.toList());
        Page<MyWritableReview> myWriteableReviewPage = new PageImpl<>(subMyWriteableReviewList, pageable, subMyWriteableReviewList.size());

        return MyWritableReviewResponse.builder()
                .myWritableReviewList(myWriteableReviewPage)
                .totalPage(totalPage)
                .build();
    }

    public MyWritableReview toMyWriteableReview(PaymentDetail paymentDetail){
        Product product = productService.findProductByProductId(paymentDetail.getProductId());
        return MyWritableReview.builder()
                .paymentDetailId(paymentDetail.getPaymentDetailId())
                .productId(product.getProductId())
                .paymentCreateDate(paymentService.findPaymentByPaymentId(paymentDetail.getPaymentId()).getPaymentDate())
                .productRepImg(product.getProductRepresentativeImgSrc())
                .productName(product.getProductName())
                .build();
    }

    public Page<ReviewPhotoResponse> reviewPhotoViewList(Pageable pageable) { // 페이징 처리한 Page<ReviewPhotoResponse> 가져옴
        Page<Review> reviewList = reviewRepository.findAll(pageable); // 페이징 처리한 Page<Review>
        Page<ReviewPhotoResponse> reviewPhotoViewList = reviewList.map(review -> new ReviewPhotoResponse(review, userService.getUsername(review.getUserId()),
                getReviewImgFindByReviewId(review.getReviewId()).isEmpty() ? null : getReviewImgFindByReviewId(review.getReviewId()).get(0).getReviewImgSrc()));
        return reviewPhotoViewList;
    }

    public ReviewShowResponse getMyReviewInfo(Long reviewId, Long productId, Long paymentDetailId){
        if(reviewId != null){
            Review review = findById(reviewId);
            List<String> reviewImgs = getReviewImgFindByReviewId(reviewId).stream().map(ReviewImg::getReviewImgSrc).collect(Collectors.toList());
            return ReviewShowResponse.builder()
                    .review(findById(reviewId))
                    .reviewImg(reviewImgs)
                    .build();
        }

        return new ReviewShowResponse(productId, paymentDetailId);
    }

}
