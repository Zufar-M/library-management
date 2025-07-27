package io.github.zufarm.library.dao;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 
    	List<Person> people = session.createQuery("FROM Person", Person.class).getResultList();
    	
    	return people;
    }
    @Transactional(readOnly = true)
    public Optional<Person> findById(int id) {
    	Session session = sessionFactory.getCurrentSession();
    	Person person = session.find(Person.class, id);
    	Hibernate.initialize(person.getBooks());
    	return Optional.ofNullable(person);
    }
    @Transactional
    public void save(Person person) {
    	Session session = sessionFactory.getCurrentSession();
    	session.persist(person);
    }
    
   @Transactional
   public void update(Person updatedPerson) {
	   Session session = sessionFactory.getCurrentSession();
	   session.merge(updatedPerson);
   }
   @Transactional
   public void deleteById(int id) {
	   Session session = sessionFactory.getCurrentSession();
	   session.remove(session.find(Person.class, id));
   }
}