package com.example.recode.controller.api;

import com.example.recode.dto.GPTQueryRequest;
import com.example.recode.service.GPTQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
public class GPTFilter {

    private final GPTQueryService gptQueryService;

    @PostMapping("/GPTQuery")
    public ResponseEntity<Object> getGPTAnswer(@RequestBody GPTQueryRequest request){
        System.err.println("query : " + request.getQuery());

        return ResponseEntity.ok()
                .body(gptQueryService.getAnswer(request).body());
    }
}
