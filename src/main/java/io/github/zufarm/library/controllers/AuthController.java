package io.github.zufarm.library.controllers;

import java.util.Collections;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	private final ModelMapper modelMapper;
	private final AuthenticationManager authenticationManager;
	
	@Autowired
	public AuthController(AppUserValidator appUserValidator, AppUserService appUserService,
							JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
		this.appUserValidator = appUserValidator;
		this.appUserService = appUserService;
		this.jwtUtil = jwtUtil;
		this.modelMapper = modelMapper;
		this.authenticationManager = authenticationManager;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}
	
	@GetMapping("/registration")
	public String registrationPage(@ModelAttribute("appUser") AppUser appUser) {
		return "auth/registration";
	}
	
	@PostMapping("/registration")
	public Map<String, String> performRegistration(@RequestBody @Valid AppUserDTO appUserDTO, BindingResult bindingResult) {
		
		
		AppUser appUser = convertToAppUser(appUserDTO);
		
		appUserValidator.validate(appUser, bindingResult);
		
		if (bindingResult.hasErrors())
			return Map.of("message", "Ошибка!");
		
		appUserService.register(appUser);
		
		String token = jwtUtil.generateToken(appUser.getUsername());
		return Map.of("jwt-token", token);
	}
	
	public AppUser convertToAppUser(AppUserDTO appUserDTO) {
		return this.modelMapper.map(appUserDTO, AppUser.class);
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
		return Map.of("jwt-token", token);
	}
	
}
