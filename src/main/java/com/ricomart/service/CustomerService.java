package com.ricomart.service;

import java.util.List;

import com.ricomart.entity.Customer;

public interface CustomerService {

	public List<Customer> getAllCustomers();

	public Customer createCustomer(Customer customer);

	public Customer updateCustomer(Long customerId, Customer customer);

	public void deleteCustomer(Long customerId);

	public Customer getCustomerById(Long customerId);

	public Customer getCustomerByEmail(String email);

	public Customer getCustomerByUsername(String username);
}
