package com.example.recode.controller.api;

import com.example.recode.dto.IdCheckRequest;
import com.example.recode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IdCheckController {

    private final UserService userService;

    @PostMapping("/checkId2")
    public ResponseEntity<Void> checkId(@RequestBody IdCheckRequest request){
        return ResponseEntity.ok()
                .build();
    }
}
