package io.github.zufarm.library.services;
import io.github.zufarm.library.dto.LoginRequest;
import io.github.zufarm.library.dto.LoginResponse;
import org.springframework.http.*;
import org.springframework.web.client.*;

public class AuthService {
	
    // TO-DO Spring spring boot
	
    private static final String AUTH_ENDPOINT = "http://localhost:8080/library/auth/login";
    private final RestTemplate restTemplate;

    public AuthService() {
        this.restTemplate = new RestTemplate();
    }

    public LoginResponse authenticate(LoginRequest request) throws RestClientException {
    	try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<LoginResponse> response = restTemplate.exchange(
            AUTH_ENDPOINT, 
            HttpMethod.POST, 
            entity, 
            LoginResponse.class
        );
        
        if (response.getStatusCode() == HttpStatus.OK) {
        	return response.getBody();
        }
        else {
        	return new LoginResponse(null, null, "Неизвестная ошибка");
        }
    	}
    	catch (HttpClientErrorException e) {
    		return new LoginResponse(null, null, e.getResponseBodyAsString());
    	}
    	
       
    }
}