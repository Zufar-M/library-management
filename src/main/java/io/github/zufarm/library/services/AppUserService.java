package io.github.zufarm.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.repositories.AppUserRepository;
import io.github.zufarm.library.util.AppUserNotFoundException;

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
		appUser.setRole("ROLE_USER");
		appUserRepository.save(appUser);
	}
	
	public List<AppUser> findAll() {
		return appUserRepository.findAll();
	}
	
	public AppUser findById(int id) {
		return appUserRepository.findById(id).orElseThrow(AppUserNotFoundException::new);
	}
	
}
