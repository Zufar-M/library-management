package io.github.zufarm.library.services;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.zufarm.library.dto.BookDTO;
import io.github.zufarm.library.exceptions.BookNotFoundException;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.models.Person;
import io.github.zufarm.library.repositories.BookRepository;

@Service
@Transactional(readOnly = true)
public class BookService {
	
private final BookRepository bookRepository;
private final PeopleService peopleService;
private final ModelMapper modelMapper;

	@Autowired
	public BookService(BookRepository bookRepository, PeopleService peopleService, ModelMapper modelMapper) {
		this.bookRepository = bookRepository;
		this.peopleService = peopleService;
		this.modelMapper = modelMapper;
	}

	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	
	public Book findOne(int id) {
		Book foundBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
	        Hibernate.initialize(foundBook.getBookHolder());
	        return foundBook;
	}

	@Transactional
	public void save(Book book) {
		bookRepository.save(book);
	}
	
	@Transactional
	public void update(int id, Book updatedBook) {
		if (!bookRepository.existsById(id)) {
			throw new BookNotFoundException(id);
		}
		updatedBook.setId(id);
		updatedBook.setBookHolder(peopleService.getBookHolder(id));
		bookRepository.save(updatedBook);
	}
	
	@Transactional
	public void delete(int id) {
		if (!bookRepository.existsById(id)) {
			throw new BookNotFoundException(id);
		}
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
		bookRepository.save(book);
	}
	
	@Transactional
	public void returnBook(int bookId) {
		Book book = findOne(bookId);
		book.setBookHolder(null);
		bookRepository.save(book);
	}
	
	public List<BookDTO> convertToBookListDTO(List<Book> books) {
		return books.stream()
        .map(book -> modelMapper.map(book, BookDTO.class))
        .collect(Collectors.toList());
	}
	
	public Book convertToBook(BookDTO bookDTO) {
		return modelMapper.map(bookDTO, Book.class);
	}
}
