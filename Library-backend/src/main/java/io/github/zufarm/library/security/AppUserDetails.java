package io.github.zufarm.library.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.zufarm.library.models.AppUser;

public class AppUserDetails implements UserDetails{

	private final AppUser appUser;
	
	public AppUserDetails(AppUser appUser) {
		this.appUser = appUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(appUser.getRole()));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return appUser.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return appUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public AppUser getAppUser() {
		return this.appUser;
	}
	
}
