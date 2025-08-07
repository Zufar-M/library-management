package io.github.zufarm.library.services;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.exceptions.UserNotFoundException;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {

	private final PeopleRepository peopleRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	public PeopleService(PeopleRepository peopleRepository, ModelMapper modelMapper) {
		this.peopleRepository = peopleRepository;
		this.modelMapper = modelMapper;
	}

	public List<Person> findAll() {
		return peopleRepository.findAll();
	}
	
	public Person findOne(int id) {
		Person foundPerson = peopleRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	        Hibernate.initialize(foundPerson.getBooks());
	        return foundPerson;
	}

	@Transactional
	public void save(Person person) {
		peopleRepository.save(person);
	}
	
	@Transactional
	public void update(int id, Person updatedPerson) {
		if (!peopleRepository.existsById(id)) {
			throw new UserNotFoundException(id);
		}
		updatedPerson.setId(id);
		peopleRepository.save(updatedPerson);
	}
	
	@Transactional
	public void delete(int id) {
		if (!peopleRepository.existsById(id)) {
			throw new UserNotFoundException(id);
		}
		peopleRepository.deleteById(id);
	}
	
	public Person getBookHolder(int bookId) {
        return peopleRepository.findPersonByBookId(bookId)
        	    .orElseThrow(() -> new UserNotFoundException("Person book holder bookId :" + bookId));
    }
	
	public List<PersonDTO> convertToListPersonDTO(List<Person> people) {
		return people.stream().map(person -> modelMapper.map(person, PersonDTO.class)).collect(Collectors.toList());
	}
	
	public boolean existsByFullNameAndBirthDate(String fullName, LocalDate birthDate) {
        return peopleRepository.existsByFullNameAndBirthDate(fullName, birthDate);
    }
	
	public Person convertToPerson(PersonDTO personDTO) {
		return modelMapper.map(personDTO, Person.class);
	}
	public PersonDTO convertToPersonDTO(Person person) {
		return modelMapper.map(person, PersonDTO.class);
	}
}
