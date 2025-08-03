package io.github.zufarm.library.services;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.repositories.BookRepository;

@Service
@Transactional(readOnly = true)
public class BookService {
	
private final BookRepository bookRepository;
private final PeopleService peopleService;

	@Autowired
	public BookService(BookRepository bookRepository, @Lazy PeopleService peopleService) {
		this.bookRepository = bookRepository;
		this.peopleService = peopleService;
	}

	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	public Book findOne(int id) {
		Optional<Book> foundBook = bookRepository.findById(id);
		if (foundBook.isPresent()) {
	        Book book = foundBook.get();
	        Hibernate.initialize(book.getBookHolder());
	        return book;
	    }
	    return null;
		
	}

	@Transactional
	public void save(Book book) {
		bookRepository.save(book);
	}
	
	@Transactional
	public void update(int id, Book updatedBook) {
		updatedBook.setId(id);
		bookRepository.save(updatedBook);
	}
	
	@Transactional
	public void delete(int id) {
		bookRepository.deleteById(id);
	}
	
	public Optional<Book> findByNameAndAuthor(String name, String author) {
		return bookRepository.findByNameAndAuthor(name, author);
	}
	
	@Transactional
	public void assingBook(int bookId, int personId) {
		Book book = findOne(bookId);
		Person person = peopleService.findOne(personId);
		book.setBookHolder(person);
	}
	
	@Transactional
	public void returnBook(int bookId) {
		Book book = findOne(bookId);
		book.setBookHolder(null);
	}
}
