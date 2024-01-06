package com.ricomart.exception;

public class UsernameNotFoundException extends RuntimeException {

	/**
	 * Employee Not Found Exception
	 */
	private static final long serialVersionUID = 1L;
	
	public UsernameNotFoundException(String message){
		super(message);
	}

}
