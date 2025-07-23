package io.github.zufarm.library.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import io.github.zufarm.library.dao.BookDAO;
import io.github.zufarm.library.models.Book;

@Controller
@RequestMapping("/books")
public class BookController {
	private final BookDAO bookDAO;
	
	@Autowired
	public BookController(BookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}
	
	@GetMapping()
	public String showBooks(Model model) {
		model.addAttribute("books", bookDAO.showAll());
        return "books/all";
    }
	
	@GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }
	
	@PostMapping()
    public String create(@ModelAttribute("book")  Book book) {
        bookDAO.save(book);
        return "redirect:/books";
    }
	
	@GetMapping("/{id}")
    public String showOne(@PathVariable("id") int id, Model model) {
		Book book = bookDAO.showOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with this id not found"));
        model.addAttribute("book", book);
        return "books/one";
    }
	
	@GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
		Book book = bookDAO.showOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with this id not found"));
        model.addAttribute("book", book);
        return "books/edit";
    }
	
	@PatchMapping("/{id}")
    public String update(@ModelAttribute("book")  Book book, @PathVariable("id") int id) {
        bookDAO.update(id, book);
        return "redirect:/books";
    }
	
	@DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
		bookDAO.delete(id);
        return "redirect:/books";
    }
	
}