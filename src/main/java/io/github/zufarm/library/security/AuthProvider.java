package io.github.zufarm.library.security;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.github.zufarm.library.services.AppUserDetailsService;

@Component
public class AuthProvider implements AuthenticationProvider{

	@Autowired
	private final AppUserDetailsService appUserDetailsService;
	
	public AuthProvider(AppUserDetailsService appUserDetailsService) {
		this.appUserDetailsService = appUserDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		
		String password = authentication.getCredentials().toString();
		
		UserDetails appUserDetails = appUserDetailsService.loadUserByUsername(username);
		
		if (!password.equals(appUserDetails.getPassword()))
			throw new BadCredentialsException("Неправильный пароль");
		
		return new UsernamePasswordAuthenticationToken(appUserDetails, password, Collections.emptyList());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
