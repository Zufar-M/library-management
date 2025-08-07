package io.github.zufarm.library.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(int userId) {
        super("User not found with id: " + userId, HttpStatus.NOT_FOUND);
    }
    public UserNotFoundException(String username) {
        super("User not found with username: " + username, HttpStatus.NOT_FOUND);
    }
}