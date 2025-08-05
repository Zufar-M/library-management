package io.github.zufarm.library.services;
import io.github.zufarm.library.dto.LoginRequest;
import io.github.zufarm.library.dto.LoginResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class AuthService {
    private static final String API_URL = "http://localhost:8080/library/auth/login";
    private final RestTemplate restTemplate = new RestTemplate();
    
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);
            ResponseEntity<LoginResponse> response = restTemplate.exchange(
                API_URL, 
                HttpMethod.POST, 
                entity, 
                LoginResponse.class
            );
            return response.getBody();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}