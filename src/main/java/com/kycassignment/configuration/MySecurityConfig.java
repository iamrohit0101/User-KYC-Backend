package com.kycassignment.configuration;

import com.kycassignment.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationFilter jwtFilter;
	@Autowired
 	private UserDetailsServiceImplementation userDetailsService;

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http
		      .csrf()
		      .disable()
		      .cors()
		      .disable()
		      .authorizeRequests()
		      .antMatchers("/addNewDocument","/registerNewUser","/getUserByUserName/{userName}","/checkUserByUserName/{userName}","/download/{fileName}").permitAll()
		      .antMatchers("/userlogin").permitAll()
		      .anyRequest().authenticated()
		      .and()
		      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.cors();

		http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticateManagerBean() throws Exception {
		return super.authenticationManagerBean();
		
	}
}
