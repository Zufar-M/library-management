package io.github.zufarm.library.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.github.zufarm.library.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
	Optional<Book> findByNameAndAuthor(String name, String author);
}
