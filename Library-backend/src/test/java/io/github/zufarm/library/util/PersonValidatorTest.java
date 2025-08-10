package io.github.zufarm.library.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.services.PeopleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class PersonValidatorTest {

    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private PersonValidator personValidator;

    @Test
    void supports_PersonClass_ReturnsTrue() {
        assertTrue(personValidator.supports(Person.class));
    }

    @Test
    void supports_NonPersonClass_ReturnsFalse() {
        assertFalse(personValidator.supports(Object.class));
    }

    @Test
    void validate_NewPerson_NoErrors() {
        Person person = new Person("Иванов Иван Иванович", "+79991234567", 
                                  LocalDate.of(1990, 1, 1));
        Errors errors = new BeanPropertyBindingResult(person, "person");
        
        when(peopleService.existsByFullNameAndBirthDate(person.getFullName(), person.getBirthDate()))
            .thenReturn(false);
        
        personValidator.validate(person, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void validate_DuplicatePerson_AddsError() {
        Person person = new Person("Петров Петр Петрович", "+79998765432", 
                                  LocalDate.of(1985, 5, 15));
        Errors errors = new BeanPropertyBindingResult(person, "person");
        
        when(peopleService.existsByFullNameAndBirthDate(person.getFullName(), person.getBirthDate()))
            .thenReturn(true);

        personValidator.validate(person, errors);

        assertTrue(errors.hasErrors());
        assertEquals("Такой читатель уже есть в библиотеке", 
                    errors.getFieldError("fullName").getDefaultMessage());
    }
}