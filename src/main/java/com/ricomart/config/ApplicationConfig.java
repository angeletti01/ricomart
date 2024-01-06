package com.ricomart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.ricomart.exception.UsernameNotFoundException;

import com.ricomart.repository.CustomerDAO;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
	
	@Autowired
	private final CustomerDAO customerDAO;
	
	ApplicationConfig(CustomerDAO customerDAO){
		this.customerDAO = customerDAO;
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
	return new UserDetailsService() {

		@Override
		public UserDetails loadUserByUsername(String username)
				throws org.springframework.security.core.userdetails.UsernameNotFoundException {
			
			return customerDAO.findByEmail(username)
					.orElseThrow(()-> new UsernameNotFoundException("User Not Found!"));
			}					
		};	
	
	}
}
