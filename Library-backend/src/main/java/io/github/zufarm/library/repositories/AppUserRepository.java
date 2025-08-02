package io.github.zufarm.library.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.github.zufarm.library.models.AppUser;



@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer>{
	Optional<AppUser> findByUsername(String name);
}
