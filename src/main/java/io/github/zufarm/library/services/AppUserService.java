package io.github.zufarm.library.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.repositories.AppUserRepository;

@Service
@Transactional(readOnly = true)
public class AppUserService {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public Optional<AppUser> findByUserName(String userName) {
		return appUserRepository.findByUsername(userName);
	}
	
	@Transactional
	public void register(AppUser appUser) {
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		appUserRepository.save(appUser);
	}
	
}
