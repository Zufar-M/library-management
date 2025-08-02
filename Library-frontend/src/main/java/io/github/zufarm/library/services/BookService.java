package io.github.zufarm.library.services;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.util.JwtTokenUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BookService {
    private static final String API_URL = "http://localhost:8080/library/books";
    private final RestTemplate restTemplate = new RestTemplate();
    
    public List<BookDTO> getAllBooks() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List<BookDTO>> response = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<BookDTO>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
}