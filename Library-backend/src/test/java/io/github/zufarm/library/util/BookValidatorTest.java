package io.github.zufarm.library.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

    @Mock
    private BookService bookService;
    
    @InjectMocks
    private BookValidator bookValidator;
    
    @Test
    void validate_WhenBookExists_AddsError() {
        Book book = new Book("Existing Book", "Author", 2020);
        when(bookService.findByNameAndAuthor("Existing Book", "Author"))
            .thenReturn(Optional.of(book));
        
        Errors errors = new BeanPropertyBindingResult(book, "book");
        bookValidator.validate(book, errors);
        
        assertTrue(errors.hasErrors());
        assertEquals("Книга с таким названием и автором уже есть в библиотеке", 
                    errors.getFieldError("name").getDefaultMessage());
    }

    @Test
    void validate_WhenBookNotExists_NoErrors() {
        Book book = new Book("New Book", "Author", 2020);
        when(bookService.findByNameAndAuthor("New Book", "Author"))
            .thenReturn(Optional.empty());
        
        Errors errors = new BeanPropertyBindingResult(book, "book");
        bookValidator.validate(book, errors);
        
        assertFalse(errors.hasErrors());
    }
}