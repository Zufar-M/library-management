package io.github.zufarm.library.controllers;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.zufarm.library.dto.AppUserDTO;
import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.services.AppUserService;
import io.github.zufarm.library.util.AppUserErrorResponse;
import io.github.zufarm.library.util.AppUserNotCreatedException;
import io.github.zufarm.library.util.AppUserNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	
	private final AppUserService appUserService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public AdminController(AppUserService appUserService, ModelMapper modelMapper) {
		this.appUserService = appUserService;
		this.modelMapper = modelMapper;
	}

	@GetMapping()
	public List<AppUserDTO> adminPage() {
		return appUserService.findAll().stream().map(this::convertToAppUserDTO).collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public AppUserDTO adminPage(@PathVariable int id) {
		return convertToAppUserDTO(appUserService.findById(id));
	}
	
	@ExceptionHandler
	private ResponseEntity<AppUserErrorResponse> handleException(AppUserNotFoundException e) {
			AppUserErrorResponse response = new AppUserErrorResponse("Person with this id was not found", System.currentTimeMillis());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping
	public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid AppUserDTO appUserDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			StringBuilder errorMsg = new StringBuilder();
			List<FieldError> errors = bindingResult.getFieldErrors();
			for (FieldError error : errors) {
				errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
			}
			throw new AppUserNotCreatedException(errorMsg.toString());
		}
		appUserService.register(convertToAppUser(appUserDTO));
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	private AppUser convertToAppUser(AppUserDTO appUserDTO) {
		return modelMapper.map(appUserDTO, AppUser.class);
	}

	@ExceptionHandler
	private ResponseEntity<AppUserErrorResponse> handleException(AppUserNotCreatedException e) {
			AppUserErrorResponse response = new AppUserErrorResponse(e.getMessage(), System.currentTimeMillis());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	private AppUserDTO convertToAppUserDTO(AppUser appUser) {
		return modelMapper.map(appUser, AppUserDTO.class);
	}
	
}
