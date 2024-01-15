package com.ricomart.config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	
	private JwtService jwtService;	
	private UserDetailsService userDetailsService;	
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {		
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}		
		
		try {			
			jwt = authHeader.substring(7);
			username = jwtService.extractUsername(jwt);
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if(jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails, 
							null,
							userDetails.getAuthorities()
							);
					authToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request)
							);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException
				| IllegalArgumentException | NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}// extract the username from JWT token
		filterChain.doFilter(request, response);
	}

}
