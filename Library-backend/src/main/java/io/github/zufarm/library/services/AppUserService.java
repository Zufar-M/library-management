package io.github.zufarm.library.services;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.zufarm.library.dto.AppUserDTO;
import io.github.zufarm.library.exceptions.UserNotFoundException;
import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.repositories.AppUserRepository;

@Service
@Transactional(readOnly = true)
public class AppUserService {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	@Autowired
	public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}
	
	public Optional<AppUser> findByUserName(String userName) {
		return appUserRepository.findByUsername(userName);
	}
	
	@Transactional
	public void register(AppUser appUser) {
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		appUserRepository.save(appUser);
	}
	
	public List<AppUser> findAll() {
		return appUserRepository.findAll();
	}
	
	public AppUser findById(int id) {
		return appUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
}
	
	
	@Transactional
	public void delete(int id) {
		if (!appUserRepository.existsById(id)) {
	        throw new UserNotFoundException(id);
	    }
		appUserRepository.deleteById(id);
	}
	
	
	@Transactional
	public void update(int id, AppUser appUserToUpdate) {
		if (!appUserRepository.existsById(id)) {
	        throw new UserNotFoundException(id);
	    }
		appUserToUpdate.setId(id);
		appUserToUpdate.setPassword(passwordEncoder.encode(appUserToUpdate.getPassword()));
		appUserRepository.save(appUserToUpdate);
	}
	
	public List<AppUserDTO> convertToAppUserDTOList(List<AppUser> appUserDTO) {
		return appUserDTO.stream()
		        .map(appUser -> {
		            AppUserDTO dto = modelMapper.map(appUser, AppUserDTO.class);
		            if (dto.getRole().equals("ROLE_ADMIN")) {
		                dto.setRole("Администратор");
		            } else {
		                dto.setRole("Сотрудник");
		            }
		            return dto;
		        })
		        .collect(Collectors.toList());
	}
	
	public AppUser convertToAppUser(AppUserDTO appUserDTO) {
		if (appUserDTO.getRole().equals("Администратор")) {
			appUserDTO.setRole("ROLE_ADMIN");
		}
		else {
			appUserDTO.setRole("ROLE_USER");
		}
		return this.modelMapper.map(appUserDTO, AppUser.class);
	}

	
}
