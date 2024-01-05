package com.ricomart.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricomart.cryptography.AES256;
import com.ricomart.custom.ApiResponse;
import com.ricomart.dto.CustomerDTO;
import com.ricomart.entity.Customer;
import com.ricomart.service.CustomerService;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/ricomart")
public class CustomerController {

	private static final Logger log = LogManager.getFormatterLogger(CustomerController.class);
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}
	
	@GetMapping("/get-all-customers")
	public List<CustomerDTO> getAllCustomers(){
		
		return customerService.getAllCustomers().stream().map(customer -> modelMapper.map(customer, CustomerDTO.class))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/get-customer-by-id/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable(name="customerId") Long customerId){
		
		Customer customer = customerService.getCustomerById(customerId);
		
		// convert entity to DTO
		CustomerDTO customerResponse = modelMapper.map(customer, CustomerDTO.class);
		
		return ResponseEntity.ok().body(customerResponse);
	}
	
	/* Included missing code from 
	 * https://www.javaguides.net/2021/02/spring-boot-dto-example-entity-to-dto.html
	 * Re-test functionality with Postman and check that an entry is made in DB */	
	
	
	@PostMapping("/create-customer")
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO){
		
		// convert JSON Request into POJO
		Customer customerRequest = modelMapper.map(customerDTO, Customer.class);
		
		AES256 aes = new AES256();
		String pWord = customerRequest.getPassword();		
		
		try {
			aes.encrypt(pWord);
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}		
		
		// Make changes to POJO
		customerRequest.setaKey(aes.getaKey());
		customerRequest.setPassword(aes.getEncryptedString());		
		
		// Store POJO (Entity) into DB
		customerService.createCustomer(customerRequest); 		
		
		return new ResponseEntity<CustomerDTO>(HttpStatus.CREATED);
	}
	@PostMapping("/login/{username}&{password}")
	public ResponseEntity<ApiResponse> loginValidate(@PathVariable(name="username") String username , @PathVariable(name="password") String password){
		
		AES256 aes = new AES256();
		Customer customer = customerService.getCustomerByUsername(username);
		
		// Debugging
		log.info("User: "+username);
		log.info("Password: "+password);
		log.info(customer);
		
		byte[] validateKey = customer.getaKey(); // key from DB
		String validatePassword = customer.getPassword(); // password entered by user
		
		aes.decrypt(validatePassword, validateKey);
		
		String decryptedPassword = aes.getDecryptedString(); // decrypted password from DB
		
		// Debugging
		log.info("Encrypted Password: "+ validatePassword);
		log.info("Decrypted Password: "+ decryptedPassword);
		log.info("User Entered Password:"+ password);
		
		ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Post deleted sucessfully", HttpStatus.OK);
		
		if(decryptedPassword.equals(password)) {
			log.info("Login Successful!!!");
			return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.ACCEPTED);
		}
		else {
			log.info("Incorrect Password!!!");
			return new ResponseEntity<ApiResponse>(HttpStatus.FORBIDDEN);
		}		
	}
	
	// Change the request for DTO and Change the response for DTO
	@PutMapping("/update-customer/{customerId}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
		
		// Convert DTO to Entity
		Customer customerRequest = modelMapper.map(customerDTO, Customer.class);
		Customer customer = customerService.updateCustomer(customerId, customerRequest);
		
		// Convert Entity to DTO
		CustomerDTO customerResponse = modelMapper.map(customer, CustomerDTO.class);
		
		return ResponseEntity.ok().body(customerResponse);
	}
	
	@DeleteMapping("/delete-customer/{customerId}")
	public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable(name = "customerId") Long customerId){
		customerService.deleteCustomer(customerId);
		ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Post deleted sucessfully", HttpStatus.OK);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}
	
}
