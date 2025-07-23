package io.github.zufarm.library.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import io.github.zufarm.library.models.Person;

@Repository
public class PersonDAO {
	
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Person> showAll() {
    	return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }
    
    public Optional<Person> showOne(int id) {
    	return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new BeanPropertyRowMapper<>(Person.class), id )
                .stream().findFirst();
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(full_name, birth_year) VALUES(?, ?)", person.getFullName(), person.getBirthYear());
    }
    
   public void update(int id, Person updatedPerson) {
	   jdbcTemplate.update("UPDATE Person SET full_name=?, birth_year=? WHERE id=?", updatedPerson.getFullName(),
               updatedPerson.getBirthYear(), id);
   }
   
   public void delete(int id) {
	   jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
   }
}