package com.example.recode.controller.api;

import com.example.recode.domain.QnA;
import com.example.recode.dto.LoginCheckResponse;
import com.example.recode.dto.QnaSubmitRequest;
import com.example.recode.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserQnaController {

    private final QnAService qnAService;

    //qna 등록
    @PostMapping("/user/qna/submit")
    public ResponseEntity<QnA> submitQna(@RequestBody QnaSubmitRequest request, Principal principal){
        return ResponseEntity.ok()
                .body(qnAService.saveQnA(request, principal));
    }

    //로그인 여부 확인
    @GetMapping("/checkLogin")
    public ResponseEntity<LoginCheckResponse> checkLogin(Principal principal){
        return ResponseEntity.ok()
                .body(LoginCheckResponse.builder()
                        .value(principal != null)
                        .build());
    }
}
