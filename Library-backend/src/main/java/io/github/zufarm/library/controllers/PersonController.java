package io.github.zufarm.library.controllers;
import java.util.List;
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
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.services.PeopleService;
import io.github.zufarm.library.util.PersonValidator;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/people")
public class PersonController {
	
	private final PeopleService peopleService;
	private final PersonValidator personValidator;
	
	@Autowired
	public PersonController(PeopleService peopleService, PersonValidator personValidator) {
		this.peopleService = peopleService;
		this.personValidator = personValidator;
	}

	@GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPeople() {
        return ResponseEntity.ok(peopleService.convertToListPersonDTO(peopleService.findAll()));
    }
	
	@PostMapping("/new")
    public ResponseEntity<?> newPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult ) {
		Person person = peopleService.convertToPerson(personDTO);
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        } 
		peopleService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }
	
	@GetMapping("/bookholder/{id}")
	public ResponseEntity<?> getBookHolder(@PathVariable("id") int bookId) {
	        Person holder = peopleService.getBookHolder(bookId);
	        return ResponseEntity.ok(peopleService.convertToPersonDTO(holder));
	}
	
	@PutMapping("/edit/{id}")
    public ResponseEntity<?> updatePerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") int id) {
		Person person = peopleService.convertToPerson(personDTO);
		personValidator.validate(person, bindingResult);
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        } 
		peopleService.update(id, person);
        return ResponseEntity.ok(person);
    }
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
	        peopleService.delete(id);
	        return ResponseEntity.noContent().build();
    }
}