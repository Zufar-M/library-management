package io.github.zufarm.library.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import io.github.zufarm.library.models.Book;
import io.github.zufarm.library.services.BookService;

@Component
public class BookValidator implements Validator{

	private final BookService bookService;
	
	@Autowired
	public BookValidator(BookService bookService) {
		this.bookService = bookService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Book book = (Book) target;
		if (bookService.findByNameAndAuthor(book.getName(), book.getAuthor()).isPresent()) {
			errors.rejectValue("name", "", "Книга с таким названием и автором уже есть в библиотеке");
		}

	}

}
