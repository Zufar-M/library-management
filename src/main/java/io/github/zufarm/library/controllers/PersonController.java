package io.github.zufarm.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.zufarm.library.dao.PersonDAO;

@Controller
@RequestMapping("/people")
public class PersonController {
	
	private final PersonDAO personDAO;
	
	@Autowired
	public PersonController(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@GetMapping()
	public String showPeople(Model model) {
		model.addAttribute("people", personDAO.showAll());
        return "people/all";
    }
	
	
}