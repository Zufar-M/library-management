package io.github.zufarm.library.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.zufarm.library.dto.AppUserDTO;
import io.github.zufarm.library.services.AppUserService;

@RestController
@RequestMapping("/users")
public class AppUserController {
	
	private final AppUserService appUserService;
	
	@Autowired
	public AppUserController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
	
	@GetMapping
	public List<AppUserDTO> getAllAppUsers() {
		return appUserService.convertToAppUserDTOList(appUserService.findAll());
	}
}
