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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/people")
public class PersonController {
	
	private final PeopleService peopleService;
	
	@Autowired
	public PersonController(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@GetMapping()
	public List<PersonDTO> getAllPeople() {
		return peopleService.convertToListPersonDTO(peopleService.findAll());
    }
	
	@PostMapping("/new")
    public ResponseEntity<?> newPerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult ) {
		Person person = peopleService.convertToPerson(personDTO);
		peopleService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Читатель успешно добавлен");
    }
	
	@GetMapping("/bookholder/{id}")
	public ResponseEntity<?> getBookHolder(@PathVariable("id") int bookId) {
	        Person holder = peopleService.getBookHolder(bookId);
	        if (holder == null) {
	        	return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(peopleService.convertToPersonDTO(holder));
	}
	
	@PutMapping("/edit/{id}")
    public ResponseEntity<?> updatePerson(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult, @PathVariable("id") int id) {
		Person person = peopleService.convertToPerson(personDTO);
		peopleService.update(id, person);
        return ResponseEntity.ok("Читатель обновлен");
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
}