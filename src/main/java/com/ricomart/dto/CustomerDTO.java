package com.ricomart.dto;

import org.springframework.context.annotation.Role;

public class CustomerDTO {
	
	private Long customerId;	

	private String firstName;	
	
	private String lastName;	
	
	private String email;	
	
	private String phone;	

	private String address;	

	private String username;	
	
	private String password;
	
	private Role role;
	
	private byte[] aKey;
	
	private byte[] bKey;
	
	private byte[] cypher;

	public CustomerDTO() {
		super();		
	}

	public CustomerDTO(Long customerId, String firstName, String lastName, String email, String phone, String address,
			String username, String password, byte[] aKey, byte[] bKey, byte[] cypher) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.username = username;
		this.password = password;
		this.aKey = aKey;
		this.bKey = bKey;
		this.cypher = cypher;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getaKey() {
		return aKey;
	}

	public void setaKey(byte[] aKey) {
		this.aKey = aKey;
	}

	public byte[] getbKey() {
		return bKey;
	}

	public void setbKey(byte[] bKey) {
		this.bKey = bKey;
	}

	public byte[] getCypher() {
		return cypher;
	}

	public void setCypher(byte[] cypher) {
		this.cypher = cypher;
	}	
	
}
