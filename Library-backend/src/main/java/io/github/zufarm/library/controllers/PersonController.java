package io.github.zufarm.library.controllers;
import java.util.List;
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
		return convertToListPersonDTO(peopleService.findAll());
    }
	
	@PostMapping("/new")
    public ResponseEntity<?> newPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult ) {
		
		Person person = convertToPerson(personDTO);
		
		//TO-DO personValidator.validate(person, bindingResult);
		
		peopleService.save(person);
		
        return ResponseEntity.status(HttpStatus.CREATED).body("Читатель успешно добавлен");
    }
	
	@PostMapping()
    public String addPerson(@ModelAttribute("person")  @Valid Person person, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }
	
	@GetMapping("/bookholder/{id}")
    public PersonDTO getBookHolder(@PathVariable("id") int Bookid) {
		return convertToPersonDTO(peopleService.getBookHolder(Bookid));
    }
	
	@PutMapping("/edit/{id}")
    public ResponseEntity<?> updatePerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") int id) {
		
		// TO-DO
//		personValidator.validate(person, bindingResult);
//		if (bindingResult.hasErrors()) {
//            return ;
//        }
		
		Person person = convertToPerson(personDTO);
		peopleService.update(id, person);
		
        return ResponseEntity.ok("Читатель обновлен");
    }
	
	@PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person")  @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
		if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.update(id, person);
        return "redirect:/people";
    }
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
		try {
	        peopleService.delete(id);
	        return ResponseEntity.ok("Читатель успешно удален!");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	               .body("Ошибка при удалении читателя!");
	    }
    }
	
	public List<PersonDTO> convertToListPersonDTO(List<Person> people) {
		return people.stream().map(person -> modelMapper.map(person, PersonDTO.class)).collect(Collectors.toList());
	}
	
	public Person convertToPerson(PersonDTO personDTO) {
		return modelMapper.map(personDTO, Person.class);
	}
	public PersonDTO convertToPersonDTO(Person person) {
		return modelMapper.map(person, PersonDTO.class);
	}
	
}