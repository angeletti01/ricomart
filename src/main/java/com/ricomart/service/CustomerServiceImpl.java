package com.ricomart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ricomart.entity.Customer;
import com.ricomart.repository.CustomerDAO;

import jakarta.persistence.EntityNotFoundException;

@Service(value ="CustomerServiceImpl")
public class CustomerServiceImpl implements CustomerService
{
	@Autowired
	private final CustomerDAO customerDAO;
	
	public CustomerServiceImpl(CustomerDAO customerDAO) {
		super();
		this.customerDAO = customerDAO;
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		
		return customerDAO.findAll();
	}
	
	@Override
	public Customer createCustomer(Customer customer) {
		
		return customerDAO.saveAndFlush(customer); 
	}
	
	@Override
	public Customer updateCustomer(Long customerId, Customer customerRequest) {
	
		Customer customer = customerDAO.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer ID: "+ customerId));
		
		customer.setFirstName(customerRequest.getFirstName());
		customer.setLastName(customerRequest.getLastName());
		customer.setEmail(customerRequest.getEmail());
		customer.setPhone(customerRequest.getPhone());
		customer.setAddress(customerRequest.getAddress());
		customer.setUsername(customerRequest.getUsername());
		customer.setPassword(customerRequest.getPassword());
		return customerDAO.saveAndFlush(customer);
	}
	
	@Override
	public void deleteCustomer(Long customerId) {
	Customer customer = customerDAO.findById(customerId)
			.orElseThrow(() -> new NoSuchElementException("Customer ID: " + customerId));
	
	customerDAO.delete(customer);
		
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		
		Optional<Customer> result = customerDAO.findById(customerId);
		
		if(result.isPresent()) {
			return result.get();
		}
		else {
			throw new EntityNotFoundException("Customer ID: " + customerId);
		}		
		
		/*	Customer customer = customerDAO.findById(customerId)
		.orElseThrow(() -> new NoSuchElementException("Customer ID: " + customerId)); */
		
		//return customer;
	}
	
	@Override
	public Customer getCustomerByEmail(String email) {
		Optional<Customer> result = customerDAO.findByEmail(email);
		
		if(result.isPresent()) {
			return result.get();
		}
		else {
			throw new EntityNotFoundException("Customer Email: " + email);
		}		
	
	}
	
	@Override
	public Customer getCustomerByUsername(String username) {
		Optional<Customer> result = customerDAO.findByUsername(username);
		
		if(result.isPresent()) {
			return result.get();
		}
		else {
			throw new EntityNotFoundException("Customer Username: " + username);
		}
	}
	
}
