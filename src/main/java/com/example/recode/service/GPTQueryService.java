package com.example.recode.service;

import com.example.recode.dto.GPTQueryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class GPTQueryService {

    @Value("${app.gk}")
    private String gk;

    public HttpResponse<String> getAnswer(GPTQueryRequest userRequest){
        HttpResponse<String> response = null;
        System.err.println(gk);
        try {
            // HttpClient 생성
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(1000))
                    .build();

            // 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .header("Authorization", gk) // 인증 헤더 설정이 필요한 경우
//                    .GET() // 또는 .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"value\"}")) 등
                    .POST(HttpRequest.BodyPublishers.ofString("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + userRequest.getQuery() + "\"}]}"))
                    .build();

            // 요청 보내고 응답 받기
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 출력
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
