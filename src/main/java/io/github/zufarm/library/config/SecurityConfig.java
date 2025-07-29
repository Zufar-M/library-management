package io.github.zufarm.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import io.github.zufarm.library.security.AuthProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

private final AuthProvider authProvider;
	
	
	@Autowired
    public SecurityConfig(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authenticationProvider(authProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()           
            )
            .formLogin(form -> form                 
                .defaultSuccessUrl("/books")                 
                .permitAll()
            );

        return http.build();
    }
}
