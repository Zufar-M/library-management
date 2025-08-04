package io.github.zufarm.library.controllers;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.services.BookService;
import io.github.zufarm.library.services.PeopleService;
import io.github.zufarm.library.util.BookValidator;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
	
	private final BookService bookService;
	private final PeopleService peopleService;
	private final BookValidator bookValidator;
	
	@Autowired
	public BookController(BookService bookService, PeopleService peopleService, BookValidator bookValidator) {
		this.bookService = bookService;
		this.peopleService = peopleService;
		this.bookValidator = bookValidator;
	}

	@GetMapping()
	public List<BookDTO> getAllBooks() {
        return bookService.convertToBookListDTO(bookService.findAll());
    }
	
	@PostMapping("/new")
    public ResponseEntity<?> newBook(@RequestBody @Valid BookDTO bookDTO, BindingResult bindingResult ) {
		Book book = bookService.convertToBook(bookDTO);
		bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body("Книга успешно создана");
    }
	
	@PutMapping("/edit/{id}")
    public ResponseEntity<?> updateBook(@RequestBody @Valid BookDTO bookDTO, BindingResult bindingResult, @PathVariable("id") int id) {
		Book book = bookService.convertToBook(bookDTO);
		bookService.update(id, book);
        return ResponseEntity.ok("Книга обновлена");
    }
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
	        bookService.delete(id);
	        return ResponseEntity.ok().build();
    }
	
	@PostMapping("/assign")
	public ResponseEntity<?> assign(@RequestBody Map<String, Integer> request) {
            Integer bookId = request.get("bookId");
            Integer personId = request.get("personId");
            bookService.assingBook(bookId, personId);
            return ResponseEntity.ok().build();  
    }
	
	@PutMapping("/return/{id}")
	public ResponseEntity<?> release(@PathVariable("id") int bookId) {
		bookService.returnBook(bookId);
		return ResponseEntity.ok().build(); 
    }
	
	@GetMapping("/holder/{personId}")
	public ResponseEntity<List<BookDTO>> getBooksByHolder(@PathVariable int personId) {
	    Person person = peopleService.findOne(personId);
	    List<BookDTO> personBooks = bookService.convertToBookListDTO(person.getBooks());
	    if (personBooks == null) {
	    	ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(personBooks);
	}
	
}