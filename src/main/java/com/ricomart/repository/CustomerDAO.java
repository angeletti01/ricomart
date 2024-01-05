package com.ricomart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricomart.entity.Customer;


@Repository(value = "CustomerDAORepository")
public interface CustomerDAO extends JpaRepository<Customer,Long> {
	
Optional<Customer> findByEmail(String email);

Optional<Customer> findByUsername(String username);


}
