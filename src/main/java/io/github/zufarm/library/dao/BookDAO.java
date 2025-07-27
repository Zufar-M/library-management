package io.github.zufarm.library.dao;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import io.github.zufarm.library.models.Book;

@Repository
public class BookDAO {
	
	private final SessionFactory sessionFactory;

    
    @Autowired
    public BookDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
    @Transactional(readOnly = true)
    public List<Book> findAll() {
    	Session session = sessionFactory.getCurrentSession();
    	 
    	List<Book> books = session.createQuery("FROM Book", Book.class).getResultList();
    	
    	return books;
    }
    
    @Transactional(readOnly = true)
    public Optional<Book> findById(int id) {
    	Session session = sessionFactory.getCurrentSession();
    	Book book = session.find(Book.class, id);
    	Hibernate.initialize(book.getBookHolder());
    	return Optional.ofNullable(book);
    }
    
    //Method for validation by name
    @Transactional(readOnly = true)
    public Optional<Book> findByName(String name) {
    	    Session session = sessionFactory.getCurrentSession();
    	    Book book = session.createQuery("FROM Book WHERE name = :name", Book.class)
    	                      .setParameter("name", name)
    	                      .uniqueResult();
    	    return Optional.ofNullable(book);
    }
    @Transactional
    public void save(Book book) {
    	Session session = sessionFactory.getCurrentSession();
    	session.persist(book);
    }
   @Transactional
   public void updateById(int id, Book updatedBook) {
	   Session session = sessionFactory.getCurrentSession();
	   Book bookToBeUpdated = session.find(Book.class, id);
	   bookToBeUpdated.setName(updatedBook.getName());
	   bookToBeUpdated.setYear(updatedBook.getYear());
	   bookToBeUpdated.setAuthor(updatedBook.getAuthor());
	   bookToBeUpdated.setBookHolder(updatedBook.getBookHolder());
   }
   @Transactional
   public void deleteById(int id) {
	   Session session = sessionFactory.getCurrentSession();
	   session.remove(session.find(Book.class, id));
   }
   
}