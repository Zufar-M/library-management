package io.github.zufarm.library.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import io.github.zufarm.library.dto.PersonDTO;
import io.github.zufarm.library.exceptions.UserNotFoundException;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PeopleService peopleService;

    @Test
    void findAll_ReturnsAllPeople() {
        Person person1 = new Person("Иванов Иван", "123456", LocalDate.now());
        Person person2 = new Person("Петров Петр", "654321", LocalDate.now());
        when(peopleRepository.findAll()).thenReturn(List.of(person1, person2));

        List<Person> result = peopleService.findAll();

        assertEquals(2, result.size());
        verify(peopleRepository).findAll();
    }

    @Test
    void findOne_WhenPersonExists_ReturnsPerson() {
        int personId = 1;
        Person person = new Person("Сидоров Сидор", "987654", LocalDate.now());
        when(peopleRepository.findById(personId)).thenReturn(Optional.of(person));

        Person result = peopleService.findOne(personId);

        assertNotNull(result);
        assertEquals(person, result);
        verify(peopleRepository).findById(personId);
    }

    @Test
    void findOne_WhenPersonNotExists_ThrowsException() {
        int personId = 99;
        when(peopleRepository.findById(personId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> peopleService.findOne(personId));
    }

    @Test
    void save_ValidPerson_SavesToRepository() {
        Person person = new Person("Новый Пользователь", "555555", LocalDate.now());

        peopleService.save(person);

        verify(peopleRepository).save(person);
    }

    @Test
    void update_ExistingPerson_UpdatesCorrectly() {
        int personId = 1;
        Person existingPerson = new Person("Старое Имя", "111111", LocalDate.now());
        Person updatedPerson = new Person("Новое Имя", "222222", LocalDate.now());
        
        when(peopleRepository.existsById(personId)).thenReturn(true);

        peopleService.update(personId, updatedPerson);

        assertEquals(personId, updatedPerson.getId());
        verify(peopleRepository).save(updatedPerson);
    }

    @Test
    void getBookHolder_WhenBookHasHolder_ReturnsPerson() {
        int bookId = 1;
        Person holder = new Person("Владелец Книги", "333333", LocalDate.now());
        when(peopleRepository.findPersonByBookId(bookId)).thenReturn(Optional.of(holder));

        Person result = peopleService.getBookHolder(bookId);

        assertNotNull(result);
        assertEquals(holder, result);
    }

    @Test
    void convertToPersonDTO_MapsCorrectly() {
        Person person = new Person("Тестовый Пользователь", "444444", LocalDate.now());
        PersonDTO personDTO = new PersonDTO();
        when(modelMapper.map(person, PersonDTO.class)).thenReturn(personDTO);

        PersonDTO result = peopleService.convertToPersonDTO(person);

        assertEquals(personDTO, result);
        verify(modelMapper).map(person, PersonDTO.class);
    }

    @Test
    void existsByFullNameAndBirthDate_WhenExists_ReturnsTrue() {
        String fullName = "Дубов Дуб";
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        when(peopleRepository.existsByFullNameAndBirthDate(fullName, birthDate)).thenReturn(true);

        boolean result = peopleService.existsByFullNameAndBirthDate(fullName, birthDate);

        assertTrue(result);
    }
}