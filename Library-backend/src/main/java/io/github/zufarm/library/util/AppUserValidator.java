package io.github.zufarm.library.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import io.github.zufarm.library.models.AppUser;
import io.github.zufarm.library.services.AppUserService;

@Component
public class AppUserValidator implements Validator{

	private final AppUserService appUserService;
	
	@Autowired
	public AppUserValidator(AppUserService appUserService) {
		this.appUserService = appUserService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return AppUser.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AppUser appUser = (AppUser) target;
		if (appUserService.findByUserName(appUser.getUsername()).isPresent()) {
			errors.rejectValue("username", "", "Такой пользователь уже зарегистрирован");
		}
	}

}
