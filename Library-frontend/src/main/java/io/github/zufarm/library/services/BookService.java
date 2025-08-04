package io.github.zufarm.library.services;
import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.util.JwtTokenUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookService {
    private static final String GET_BOOKS_URL = "http://localhost:8080/library/books";
    private static final String ADD_BOOK_URL = "http://localhost:8080/library/books/new";
    private static final String EDIT_BOOK_URL = "http://localhost:8080/library/books/edit/";
    private static final String DELETE_BOOK_URL = "http://localhost:8080/library/books/delete/";
    private static final String RETURN_BOOK_URL = "http://localhost:8080/library/books/return/";
    private static final String ASSIGN_BOOK_URL = "http://localhost:8080/library/books/assign";
    private static final String GET_BOOKS_BY_HOLDER_URL = "http://localhost:8080/library/books/holder/";
    private final RestTemplate restTemplate = new RestTemplate();
    
    
    public List<BookDTO> getAllBooks() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List<BookDTO>> response = restTemplate.exchange(
                GET_BOOKS_URL,
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
    
    public boolean addBook(BookDTO book) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<BookDTO> entity = new HttpEntity<>(book, headers);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                ADD_BOOK_URL,
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
	    public boolean updateBook(BookDTO book) {
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            HttpEntity<BookDTO> entity = new HttpEntity<>(book, headers);
	            
	            ResponseEntity<Void> response = restTemplate.exchange(
	                EDIT_BOOK_URL + book.getId(),
	                HttpMethod.PUT,
	                entity,
	                Void.class
	            );
	            
	            return response.getStatusCode() == HttpStatus.OK;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	    
	    public boolean deleteBook(int id) {
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
	            
	            HttpEntity<String> entity = new HttpEntity<>(headers);
	            
	            ResponseEntity<Void> response = restTemplate.exchange(
	                DELETE_BOOK_URL + id,
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
	    public boolean returnBook(int bookId) {
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            HttpEntity<String> entity = new HttpEntity<>(headers);
	            
	            ResponseEntity<Void> response = restTemplate.exchange(
	                RETURN_BOOK_URL + bookId,
	                HttpMethod.PUT,
	                entity,
	                Void.class
	            );
	            
	            return response.getStatusCode() == HttpStatus.OK;
	            
	        } catch (HttpClientErrorException.NotFound e) {
	            System.err.println("Книга не найдена или не выдана: " + bookId);
	            return false;
	        } 
	    }
	    
	    public boolean assignBook(int bookId, int personId) {
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            Map<String, Object> requestBody = new HashMap<>();
	            requestBody.put("bookId", bookId);
	            requestBody.put("personId", personId);
	            
	            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
	            
	            ResponseEntity<Void> response = restTemplate.exchange(
	                ASSIGN_BOOK_URL,
	                HttpMethod.POST,
	                entity,
	                Void.class
	            );
	            
	            return response.getStatusCode() == HttpStatus.OK;
	            
	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
	                System.err.println("Ошибка выдачи книги: " + e.getResponseBodyAsString());
	            }
	            return false;
	        } 
	    }
	    
	    public List<BookDTO> getBooksByHolder(int personId) {
	        try {
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "Bearer " + JwtTokenUtil.getToken());
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            HttpEntity<String> entity = new HttpEntity<>(headers);
	            
	            ResponseEntity<List<BookDTO>> response = restTemplate.exchange(
	                GET_BOOKS_BY_HOLDER_URL + personId,
	                HttpMethod.GET,
	                entity,
	                new ParameterizedTypeReference<List<BookDTO>>() {}
	            );
	            
	            if (response.getStatusCode() == HttpStatus.OK) {
	                return response.getBody();
	            }
	        } catch (HttpClientErrorException.NotFound e) {
	            return List.of();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return List.of();
	    }
    
}