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
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.services.PeopleService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/people")
public class PersonController {
	
	private final PeopleService peopleService;
	private final ModelMapper modelMapper;

	@Autowired
	public PersonController(PeopleService peopleService, ModelMapper modelMapper) {
		this.peopleService = peopleService;
		this.modelMapper = modelMapper;
	}

	@ResponseBody
	@GetMapping()
	public List<PersonDTO> getAllPeople() {
		return convertToPersonDTO(peopleService.findAll());
    }
	
	@GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }
	
	@PostMapping()
    public String addPerson(@ModelAttribute("person")  @Valid Person person, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }
	
	@GetMapping("/{id}")
    public String getPersonById(@PathVariable("id") int id, Model model) {
		Person person = peopleService.findOne(id);
        model.addAttribute("person", person);
        model.addAttribute("personBooks", person.getBooks());
        return "people/one";
        
    }
	
	@GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
		Person person = peopleService.findOne(id);
        model.addAttribute("person", person);
        return "people/edit";
    }
	
	@PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person")  @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
		if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }
	
	@DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
	
	public List<PersonDTO> convertToPersonDTO(List<Person> people) {
		return people.stream().map(person -> modelMapper.map(person, PersonDTO.class)).collect(Collectors.toList());
	}
	
}