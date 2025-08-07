package io.github.zufarm.library.services;
import io.github.zufarm.library.dto.UserDTO;
import io.github.zufarm.library.util.JwtTokenUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class UserService {
    private static final String REGISTER_USER_URL = "http://localhost:8080/library/auth/registration";
    private static final String ALL_USERS_URL = "http://localhost:8080/library/users";
    private static final String EDIT_USER_URL = "http://localhost:8080/library/users/edit/";
    private static final String DELETE_USER_URL = "http://localhost:8080/library/users/delete/";
    private final RestTemplate restTemplate = new RestTemplate();

    // TO-DO Spring spring boot, response error code handler
    
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
    
    public void updateUser(UserDTO user) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                EDIT_USER_URL + user.getId(),
                HttpMethod.PUT,
                entity,
                Void.class
            );        
        } catch (Exception e) {
            e.printStackTrace();    
        }
    }
    public boolean deleteUser(int id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                DELETE_USER_URL + id,
                HttpMethod.DELETE,
                entity,
                Void.class
            );
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}