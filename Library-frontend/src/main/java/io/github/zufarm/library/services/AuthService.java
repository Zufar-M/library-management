package io.github.zufarm.library.services;

import io.github.zufarm.library.dto.JwtResponse;
import io.github.zufarm.library.dto.LoginRequest;
import io.github.zufarm.library.util.JwtTokenUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class AuthService {
    private static final String API_URL = "http://localhost:8080/library/auth/login";
    private final RestTemplate restTemplate = new RestTemplate();
    
    public JwtResponse login(LoginRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<JwtResponse> response = restTemplate.exchange(
                API_URL, 
                HttpMethod.POST, 
                entity, 
                JwtResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JwtTokenUtil.setToken(response.getBody().getToken());
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}