package io.github.zufarm.library.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.services.PeopleService;
@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;
    
    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
     
        if (peopleService.existsByFullNameAndBirthDate(person.getFullName(), person.getBirthDate())) {
            errors.rejectValue("fullName", "", "Такой читатель уже есть в библиотеке");
        }
    }
}