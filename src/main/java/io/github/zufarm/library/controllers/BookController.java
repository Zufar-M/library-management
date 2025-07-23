package io.github.zufarm.library.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.zufarm.library.dao.BookDAO;

@Controller
@RequestMapping("/books")
public class BookController {
	private final BookDAO bookDAO;
	
	@Autowired
	public BookController(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}
	
	@GetMapping()
	public String showBooks() {
        return "books/all";
    }
}