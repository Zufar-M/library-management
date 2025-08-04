package io.github.zufarm.library.services;

import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.dto.UserDTO;
import io.github.zufarm.library.util.JwtTokenUtil;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class UserService {
    private static final String REGISTER_USER_URL = "http://localhost:8080/library/auth/registration";
    private static final String ALL_USERS_URL = "http://localhost:8080/library/users";
    private final RestTemplate restTemplate = new RestTemplate();

    public void registerUser(UserDTO user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);
        restTemplate.exchange(
        		REGISTER_USER_URL, 
        		HttpMethod.POST,
        		entity, 
        		Void.class
        		);
    }

    public List<UserDTO> getAllUsers() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> entity = new HttpEntity<>(headers);
    	ResponseEntity<List<UserDTO>> response = restTemplate.exchange(
        	ALL_USERS_URL, 
        	HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<UserDTO>>() {}
        );
        return response.getBody();
    }
}