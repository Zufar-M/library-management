package io.github.zufarm.library.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
        	.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/error").permitAll()
                .anyRequest().authenticated()           
            )
            .formLogin(form -> form
            	.loginPage("/auth/login")
            	.loginProcessingUrl("/library/auth/login")
                .defaultSuccessUrl("/books", true)
                .failureUrl("/auth/login?error")
                .permitAll()
            );
        return http.build();
    }
	
	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance(); 
    }
}
