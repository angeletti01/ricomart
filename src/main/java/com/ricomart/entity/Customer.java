package com.ricomart.entity;

import java.io.Serializable;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="customer", schema="ricomart")
public class Customer implements UserDetails, Serializable{		
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="customer_id")
	private Long customerId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="address")
	private String address;
	
	@Column(name="user_name")
	private String username;
	
	@Column(name = "passkey")
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name="role")
	private Role role;
	
	@Column(name = "a_key")
	private byte[] aKey;
	
	@Column(name = "b_key")
	private byte[] bKey;
	
	@Column(name = "cypher_object")
	private byte[] cypher;	

	public Customer() {
		super();		
	}	
	
	public Customer(Long customerId, String firstName, String lastName, String email, String phone, String address,
			String username, String password, Role role, byte[] aKey, byte[] bKey, byte[] cypher) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.username = username;
		this.password = password;
		this.role = role;
		this.aKey = aKey;
		this.bKey = bKey;
		this.cypher = cypher;
	}



	/**User Details Methods from Spring Security*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {	
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}

	@Override
	public boolean isEnabled() {		
		return true;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	
}
