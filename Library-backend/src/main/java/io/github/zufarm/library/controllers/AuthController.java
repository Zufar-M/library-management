package io.github.zufarm.library.controllers;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.zufarm.library.dto.AppUserDTO;
import io.github.zufarm.library.dto.AuthenticationDTO;
import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.security.JWTUtil;
import io.github.zufarm.library.services.AppUserService;
import io.github.zufarm.library.util.AppUserValidator;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AppUserValidator appUserValidator;
	private final AppUserService appUserService;
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	
	@Autowired
	public AuthController(AppUserValidator appUserValidator, AppUserService appUserService,
							JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
		this.appUserValidator = appUserValidator;
		this.appUserService = appUserService;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}


	@PostMapping("/registration")
	public ResponseEntity<?> performRegistration(@RequestBody @Valid AppUserDTO appUserDTO, BindingResult bindingResult) {
		AppUser appUser = appUserService.convertToAppUser(appUserDTO);
		appUserService.register(appUser);
		return ResponseEntity.ok().build();
	}
	
	
	
	@PostMapping("/login")
	public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
		UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());
		try {
		authenticationManager.authenticate(authInputToken);
		}
		catch (BadCredentialsException e) {
			return Map.of("message", "Incorrect credentials!");
		}
		String token = jwtUtil.generateToken(authenticationDTO.getUsername());
		String userRole = appUserService.findByUserName(authenticationDTO.getUsername()).get().getRole();
		return Map.of("token", token, "role", userRole);
	}
	
}
