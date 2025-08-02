package io.github.zufarm.library.services;
import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.util.JwtTokenUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class PersonService {
    private static final String API_URL = "http://localhost:8080/library/people";
    private static final String ADD_PERSON_URL = "http://localhost:8080/library/people/new";
    private final RestTemplate restTemplate = new RestTemplate();
    
    public List<PersonDTO> getAllPeople() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List<PersonDTO>> response = restTemplate.exchange(
                API_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PersonDTO>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }
    
    public boolean addPerson(PersonDTO person) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<PersonDTO> entity = new HttpEntity<>(person, headers);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                ADD_PERSON_URL,
                HttpMethod.POST,
                entity,
                Void.class
            );
            
            return response.getStatusCode() == HttpStatus.CREATED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}