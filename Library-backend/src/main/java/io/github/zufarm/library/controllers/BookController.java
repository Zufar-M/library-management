package io.github.zufarm.library.controllers;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
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
	
	@GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
		Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        return "books/edit";
    }
	
	@PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book")  @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
		bookValidator.validate(book, bindingResult);
		if (bindingResult.hasErrors()) {
            return "books/edit";
        }
		Person bookHolder = bookService.findOne(id).getBookHolder();
        book.setBookHolder(bookHolder);
		bookService.update(id, book);
        return "redirect:/books";
    }
	
	@DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
		bookService.delete(id);
        return "redirect:/books";
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
}