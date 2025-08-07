package io.github.zufarm.library.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends ApiException {
    public BookNotFoundException(int bookId) {
        super("Book not found with id: " + bookId, HttpStatus.NOT_FOUND);
    }
    public BookNotFoundException(String name) {
        super("Book not found with name: " + name, HttpStatus.NOT_FOUND);
    }
}