package com.example.recode.service;

import com.example.recode.domain.*;
import com.example.recode.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final HistoryRepository historyRepository;
    private final NoticeRepository noticeRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final PaymentRepository paymentRepository;
    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final QnARepository qnARepository;
    private final ReviewImgRepository reviewImgRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<Address> findAllAddress(){
        return addressRepository.findAll();
    }
    public List<Cart> findAllCart() {return cartRepository.findAll();}
    public List<History> findAllHistory() {return historyRepository.findAll();}
    public List<Notice> findAllNotice() {return noticeRepository.findAll();}
    public List<PaymentDetail> findAllPaymentDetail() {return paymentDetailRepository.findAll();}
    public List<Payment> findAllPayment() {return paymentRepository.findAll();}
    public List<ProductImg> findAllProductImg() {return productImgRepository.findAll();}
    public List<Product> findAllProduct(){
        return productRepository.findAll();
    }
    public List<QnA> findAllQnA(){
        return qnARepository.findAll();
    }
    public List<ReviewImg> findAllReviewImg() {return reviewImgRepository.findAll();}
    public List<Review> findAllReview() {return reviewRepository.findAll();}
    public List<User> findAllUser() {return userRepository.findAll();}



    public void saveQnA(QnA qnA) {qnARepository.save(qnA);}
}
