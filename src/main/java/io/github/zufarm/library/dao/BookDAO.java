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
    
    public List<Book> findAll() {
    	return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }
    
    public Optional<Book> findById(int id) {
    	return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new BeanPropertyRowMapper<>(Book.class), id )
                .stream().findFirst();
    }
    //Overload method for validation by name
    public Optional<Book> findByName(String name) {
    	return jdbcTemplate.query("SELECT * FROM Book WHERE name=?", new BeanPropertyRowMapper<>(Book.class), name )
                .stream().findFirst();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(name, author, year) VALUES(?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }
    
   public void updateById(int id, Book updatedBook) {
	   jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=?, person_id=? WHERE id=?", updatedBook.getName(), 
			   													updatedBook.getAuthor(), updatedBook.getYear(), updatedBook.getPersonId(), id);
   }
   
   public void deleteById(int id) {
	   jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
   }
}