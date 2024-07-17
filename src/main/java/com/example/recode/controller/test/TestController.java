package com.example.recode.controller.test;

import com.example.recode.domain.Address;
import com.example.recode.domain.QnA;
import com.example.recode.service.GPTQueryService;
import com.example.recode.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public String test (Model model){

        List<Address> addresses = testService.findAllAddress();
//        testService.saveQnA(QnA.builder()
//                .userId(1)
//                .productId(2)
//                .qnAQuestionTitle("inputTitle")
//                .qnAQuestionContent("input Question content")
//                .qnAAnswer("input answer")
//                .qnAViews(3)
//                .build());




        model.addAttribute("addresses", addresses);
        model.addAttribute("carts", testService.findAllCart());
        model.addAttribute("historys", testService.findAllHistory());
        model.addAttribute("notices", testService.findAllNotice());
        model.addAttribute("payments", testService.findAllPayment());
        model.addAttribute("paymentDetails", testService.findAllPaymentDetail());
        model.addAttribute("products", testService.findAllProduct());
        model.addAttribute("productImgs", testService.findAllProductImg());
        model.addAttribute("QnAs", testService.findAllQnA());
        model.addAttribute("reviews", testService.findAllReview());
        model.addAttribute("reviewImgs", testService.findAllReviewImg());
        model.addAttribute("users", testService.findAllUser());

        return "test/test";
    }

}
