package com.example.recode.service;

import com.example.recode.domain.Key;
import com.example.recode.dto.GPTQueryRequest;
import com.example.recode.repository.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GPTQueryService {

    private final KeyRepository keyRepository;
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    // 키 생성
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();

        System.err.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        System.err.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        System.err.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // 주어진 키를 사용하여 데이터를 암호화
    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 주어진 키를 사용하여 데이터를 복호화
    public static String decrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public Key findKeyByKeyName(String keyName){
        return keyRepository.findByKeyName(keyName)
                .orElseThrow(() -> new IllegalArgumentException("not found Key"));
    }

    public HttpResponse<String> getAnswer(GPTQueryRequest userRequest){
        HttpResponse<String> response = null;
        Key apiKey = findKeyByKeyName("OpenApi");
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
                    .header("Authorization", "Bearer " + decrypt(apiKey.getKeyCipheredKey(), apiKey.getKeyDecryptionKey())) // 인증 헤더 설정이 필요한 경우
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
