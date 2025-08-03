package io.github.zufarm.library.controllers;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.services.BookService;
import io.github.zufarm.library.services.PeopleService;
import io.github.zufarm.library.util.BookValidator;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;
	private final PeopleService peopleService;
	private final BookValidator bookValidator;
	private final ModelMapper modelMapper;
	@Autowired
	public BookController(BookService bookService, PeopleService peopleService, BookValidator bookValidator, ModelMapper modelMapper) {
		this.bookService = bookService;
		this.peopleService = peopleService;
		this.bookValidator = bookValidator;
		this.modelMapper = modelMapper;
	}

	@GetMapping()
	@ResponseBody
	public List<BookDTO> getAllBooks() {
        return convertToBookDTO(bookService.findAll());
    }
	
	@PostMapping("/new")
    public ResponseEntity<?> newBook(@RequestBody @Valid BookDTO bookDTO, BindingResult bindingResult ) {
		
		Book book = convertToBook(bookDTO);
		
		//TO-DO bookValidator.validate(book, bindingResult);
		
		bookService.save(book);
		
        return ResponseEntity.status(HttpStatus.CREATED).body("Книга успешно создана");
    }
	
	@PostMapping()
    public String addBook(@ModelAttribute("book")  @Valid Book book, BindingResult bindingResult) {
		bookValidator.validate(book, bindingResult);
		if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }
	
	@GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model) {
		Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.findAll());
        
        return "books/one";
    }
	
	
	
	@PutMapping("/edit/{id}")
    public ResponseEntity<?> updateBook(@RequestBody @Valid BookDTO bookDTO, BindingResult bindingResult, @PathVariable("id") int id) {
		
		// TO-DO
//		bookValidator.validate(book, bindingResult);
//		if (bindingResult.hasErrors()) {
//            return "books/edit";
//        }
		
		Book book = convertToBook(bookDTO);
		bookService.update(id, book);
		
        return ResponseEntity.ok("Книга обновлена");
    }
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
		try {
	        bookService.delete(id);
	        return ResponseEntity.ok("Книга успешно удалена");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	               .body("Ошибка при удалении книги");
	    }
    }
	
	@PostMapping("/{id}/assign")
	public String assign(@PathVariable("id") int bookId, @RequestParam("personId") int personId) {
		Book book = bookService.findOne(bookId);
		book.setBookHolder(peopleService.findOne(personId));
		bookService.update(bookId, book);
        return "redirect:/books/" + bookId;
    }
	@PostMapping("/{id}/release")
	public String release(@PathVariable("id") int bookId) {
		Book book = bookService.findOne(bookId);
		book.setBookHolder(null);
		bookService.update(bookId, book);
        return "redirect:/books/" + bookId;
    }
	public List<BookDTO> convertToBookDTO(List<Book> books) {
		return books.stream()
        .map(book -> modelMapper.map(book, BookDTO.class))
        .collect(Collectors.toList());
	}
	
	public Book convertToBook(BookDTO bookDTO) {
		return modelMapper.map(bookDTO, Book.class);
	}
}