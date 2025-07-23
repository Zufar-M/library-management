package io.github.zufarm.library.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import io.github.zufarm.library.models.Book;

@Repository
public class BookDAO {
	
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Book> showAll() {
    	return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }
    
    public Optional<Book> showOne(int id) {
    	return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), id )
                .stream().findFirst();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(name, author, year) VALUES(?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }
    
   public void update(int id, Book updatedBook) {
	   jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=? WHERE id=?", updatedBook.getName(), 
			   													updatedBook.getAuthor(), updatedBook.getYear(), id);
   }
   
   public void delete(int id) {
	   jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
   }
}