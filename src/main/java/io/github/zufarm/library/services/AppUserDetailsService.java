package io.github.zufarm.library.services;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.repositories.AppUserRepository;
import io.github.zufarm.library.security.AppUserDetails;

@Service
public class AppUserDetailsService implements UserDetailsService{

	private final AppUserRepository appUserRepository;

	@Autowired
	public AppUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AppUser> appUser = appUserRepository.findByUsername(username);
		
		if (appUser.isEmpty())
			throw new UsernameNotFoundException("Пользователь не найден!");
		
		return new AppUserDetails(appUser.get());
	}
	
}
