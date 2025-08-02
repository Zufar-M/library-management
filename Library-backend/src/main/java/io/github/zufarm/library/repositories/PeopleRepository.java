package io.github.zufarm.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.zufarm.library.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer>{
	
}
