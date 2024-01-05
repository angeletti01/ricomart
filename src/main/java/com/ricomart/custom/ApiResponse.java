package com.ricomart.custom;

import org.springframework.http.HttpStatus;

public class ApiResponse {

	public Boolean response;
	public String message;
	public HttpStatus status;
	
	public ApiResponse(Boolean response, String message, HttpStatus status){
		this.response = response;
		this.message = message;
		this.status = status;
	}

}
