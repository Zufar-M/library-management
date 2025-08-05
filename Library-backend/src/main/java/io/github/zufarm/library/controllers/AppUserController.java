package io.github.zufarm.library.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.zufarm.library.dto.AppUserDTO;
import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.services.AppUserService;
import jakarta.validation.Valid;

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
	
	@PutMapping("/edit/{id}")
    public ResponseEntity<?> updateAppUser(@RequestBody @Valid AppUserDTO appUserDTO, BindingResult bindingResult, @PathVariable("id") int id) {
		AppUser appUser = appUserService.convertToAppUser(appUserDTO);
		appUserService.update(id, appUser);
        return ResponseEntity.ok("Читатель обновлен");
    }
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
	        appUserService.delete(id);
	        return ResponseEntity.ok().build();
    }
}
