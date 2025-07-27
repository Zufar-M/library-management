package io.github.zufarm.library.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.github.zufarm.library.models.Person;

@Repository
public class PersonDAO {
	
    private final SessionFactory sessionFactory;

    
    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
    
    @Transactional(readOnly = true)
	public List<Person> findAll() {
    	Session session = sessionFactory.getCurrentSession();
 
    	List<Person> people = session.createQuery("select from Person", Person.class).getResultList();
    	
    	return people;
    }
    
    public Optional<Person> findById(int id) {
    	return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new BeanPropertyRowMapper<>(Person.class), id )
                .stream().findFirst();
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(full_name, birth_year) VALUES(?, ?)", person.getFullName(), person.getBirthYear());
    }
    
   public void updateById(int id, Person updatedPerson) {
	   jdbcTemplate.update("UPDATE Person SET full_name=?, birth_year=? WHERE id=?", updatedPerson.getFullName(),
               updatedPerson.getBirthYear(), id);
   }
   
   public void deleteById(int id) {
	   jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
   }
}