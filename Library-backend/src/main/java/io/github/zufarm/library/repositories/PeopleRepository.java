package io.github.zufarm.library.repositories;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import io.github.zufarm.library.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer>{
	@Query("SELECT p FROM Person p JOIN p.books b WHERE b.id = :bookId")
	Optional<Person> findPersonByBookId(@Param("bookId") int bookId);
	
	boolean existsByFullNameAndBirthDate(String fullName, LocalDate birthDate);
	
}
