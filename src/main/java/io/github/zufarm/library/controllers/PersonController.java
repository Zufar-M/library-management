package io.github.zufarm.library.controllers;
import java.util.List;
import java.util.stream.Collectors;

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
import io.github.zufarm.library.dao.PersonDAO;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;

@Controller
@RequestMapping("/people")
public class PersonController {
	
	private final BookDAO bookDAO;
	private final PersonDAO personDAO;
	
	@Autowired
	public PersonController(BookDAO bookDAO, PersonDAO personDAO) {
		this.bookDAO = bookDAO;
		this.personDAO = personDAO;
	}

	@GetMapping()
	public String showPeople(Model model) {
		model.addAttribute("people", personDAO.showAll());
        return "people/all";
    }
	
	@GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }
	
	@PostMapping()
    public String create(@ModelAttribute("person")  Person person) {
        personDAO.save(person);
        return "redirect:/people";
    }
	
	@GetMapping("/{id}")
    public String showOne(@PathVariable("id") int id, Model model) {
		Person person = personDAO.showOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with this id not found"));
        model.addAttribute("person", person);
        
        //Logic of finding books given to person
        //This logic may be placed in BookDAO as alternative
        List<Book> personBooks = bookDAO.showAll().stream().filter(book -> book.getPersonId() != null && book.getPersonId() == id).collect(Collectors.toList());
        model.addAttribute("personBooks", personBooks);
        
        return "people/one";
        
    }
	
	@GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
		Person person = personDAO.showOne(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with this id not found"));
        model.addAttribute("person", person);
        return "people/edit";
    }
	
	@PatchMapping("/{id}")
    public String update(@ModelAttribute("person")  Person person, @PathVariable("id") int id) {
        personDAO.update(id, person);
        return "redirect:/people";
    }
	
	@DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
	
}