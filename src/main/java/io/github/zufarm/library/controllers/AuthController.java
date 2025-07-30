package io.github.zufarm.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.services.AppUserService;
import io.github.zufarm.library.util.AppUserValidator;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final AppUserValidator appUserValidator;
	private final AppUserService appUserService;
	
	@Autowired
	public AuthController(AppUserValidator appUserValidator, AppUserService appUserService) {
		this.appUserValidator = appUserValidator;
		this.appUserService = appUserService;
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
	public String performRegistration(@ModelAttribute("appUser") @Valid AppUser appUser, BindingResult bindingResult) {
		appUserValidator.validate(appUser, bindingResult);
		
		if (bindingResult.hasErrors())
			return "/auth/registration";
		
		appUserService.register(appUser);
		
		return "redirect:/auth/login";
	}
	
}
