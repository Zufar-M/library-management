package io.github.zufarm.library.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.github.zufarm.library.security.JWTUtil;
import io.github.zufarm.library.services.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	private final AppUserDetailsService appUserDetailsService;
	
	@Autowired
	public JWTFilter(JWTUtil jwtUtil, AppUserDetailsService appUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.appUserDetailsService = appUserDetailsService;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		
		if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			if (jwt.isBlank()) {
				response.sendError(response.SC_BAD_REQUEST, "Invalid JWT Token");
			}
			else {
				try {
					String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
					UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					if (SecurityContextHolder.getContext().getAuthentication() == null) {
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
				catch (JWTVerificationException exc) {
					response.sendError(response.SC_BAD_REQUEST, "Invalid JWT Token");
				}
			}  
		}
		filterChain.doFilter(request, response);
		
	}
	
}
