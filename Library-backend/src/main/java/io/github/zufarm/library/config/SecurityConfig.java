package io.github.zufarm.library.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.github.zufarm.library.services.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

private final AppUserDetailsService appUserDetailsService;
private final JWTFilter jwtFilter;
	
	@Autowired
	public SecurityConfig(AppUserDetailsService appUserDetailsService, JWTFilter jwtFilter) {
		this.appUserDetailsService = appUserDetailsService;
		this.jwtFilter = jwtFilter;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            	.authorizeHttpRequests(auth -> auth
            	.requestMatchers("/auth/login").permitAll() 
            	.requestMatchers("/admin").hasRole("ADMIN")   	
                .anyRequest().hasAnyRole("USER", "ADMIN")
            	)
            	.csrf(csrf -> csrf.disable())
        		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            	
     
        return http.build();
    }
	
	@Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {
        
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder
            .userDetailsService(appUserDetailsService)
            .passwordEncoder(passwordEncoder);
        
        return authenticationManagerBuilder.build();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
    }
	
}
