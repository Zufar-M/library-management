package io.github.zufarm.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.zufarm.library.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer>{
	@Query("SELECT p FROM Person p JOIN p.books b WHERE b.id = :bookId")
    Person findPersonByBookId(@Param("bookId") int bookId);
	
}
