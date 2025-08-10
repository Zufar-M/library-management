package io.github.zufarm.library.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import io.github.zufarm.library.exceptions.BookNotFoundException;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private PeopleService peopleService;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private BookService bookService;

    @Test
    void findOne_WhenBookExists_ReturnsBook() {
        Book mockBook = new Book("Test Book", "Author", 2020);
        when(bookRepository.findById(1)).thenReturn(Optional.of(mockBook));
        
        Book result = bookService.findOne(1);
        
        assertEquals(mockBook, result);
    }

    @Test
    void findOne_WhenBookNotExists_ThrowsException() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());
        
        assertThrows(BookNotFoundException.class, () -> bookService.findOne(1));
    }

    @Test
    void save_ValidBook_SavesToRepository() {
        Book bookToSave = new Book("New Book", "Author", 2021);
        
        bookService.save(bookToSave);
        
        verify(bookRepository).save(bookToSave);
    }

    @Test
    void update_ExistingBook_UpdatesCorrectly() {
        Book existingBook = new Book("Old Title", "Author", 2020);
        existingBook.setId(1);
        when(bookRepository.existsById(1)).thenReturn(true);
        when(peopleService.getBookHolder(1)).thenReturn(null);
        
        Book updatedBook = new Book("New Title", "Author", 2021);
        bookService.update(1, updatedBook);
        
        verify(bookRepository).save(updatedBook);
        assertEquals(1, updatedBook.getId());
    }

    @Test
    void assignBook_ValidIds_AssignsBookToPerson() {
        Book book = new Book("Book", "Author", 2020);
        Person person = new Person();
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(peopleService.findOne(1)).thenReturn(person);
        
        bookService.assingBook(1, 1);
        
        assertEquals(person, book.getBookHolder());
        verify(bookRepository).save(book);
    }
}