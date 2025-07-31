package io.github.zufarm.library.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import io.github.zufarm.library.services.AppUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

private final AppUserDetailsService appUserDetailsService;
	
	@Autowired
	public SecurityConfig(AppUserDetailsService appUserDetailsService) {
		this.appUserDetailsService = appUserDetailsService;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/error", "/auth/registration").permitAll()
                .anyRequest().authenticated()           
            )
            .formLogin(form -> form
            	.loginPage("/auth/login")
            	.loginProcessingUrl("/library/auth/login")
                .defaultSuccessUrl("/books", true)
                .failureUrl("/auth/login?error")
            ).logout(logout -> logout
                    .logoutUrl("/library/auth/logout")
                    .logoutSuccessUrl("/auth/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .clearAuthentication(true)
                    .permitAll()
                );
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
