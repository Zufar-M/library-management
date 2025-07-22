package io.github.zufarm.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.zufarm.library.dao.PersonDAO;

@Controller
@RequestMapping("/books")
public class BookController {
	private final PersonDAO personDAO;
	
	@Autowired
	public BookController(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
	@GetMapping()
	public String showBooks() {
        personDAO.testConnection();
        return "books/all";
    }
}