package com.custManagent.controller;

import com.custManagent.auth.JwtTokenUtil;
import com.custManagent.dto.CustomerDto;
import com.custManagent.entity.Customer;
import com.custManagent.entity.JwtResponse;
import com.custManagent.repository.CustomerRepository;
import com.custManagent.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	CustomerRepository customerRepository;

	// note : roleId = 1 for admin,and roleId = 2 for Customer

	@PostMapping("/createCustomer")
	public ResponseEntity<?> createCustomer(HttpServletRequest request, @RequestParam(required = true) String name,
			@RequestParam(required = true) String email, @RequestParam(required = true) String phoneNumber) {
		try {

			// to get tokenn from authorization header
			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) { // only for admin

					Customer customerInfo = customerService.createCustomer(name, email, phoneNumber);
					return new ResponseEntity<>(customerInfo, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error creating customer: ", e);
			return new ResponseEntity<>("Error creating customer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/updateCustomerById")
	public ResponseEntity<?> updateCustomerById(HttpServletRequest request, @RequestParam(required = true) Long id,
			@RequestParam(required = true) String name, @RequestParam(required = true) String email,
			@RequestParam(required = true) String phoneNumber) {
		try {
			// to get from authorization header
			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {

					Customer updateInfo = customerService.updateCustomerById(id, name, email, phoneNumber);
					return new ResponseEntity<>(updateInfo, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in update customer api: ", e);
			return new ResponseEntity<>("Error in update customer api..!! " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteCustomerById")
	public ResponseEntity<?> deleteCustomerById(HttpServletRequest request, @RequestParam(required = true) Long id) {
		try {

			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {

					String DelCustomerById = customerService.deleteCustomerById(id);
					return new ResponseEntity<>(DelCustomerById, HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in delete customer api: ", e);
			return new ResponseEntity<>("Error in delete customer api..!! " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCustomerById")
	public ResponseEntity<?> getCustomerById(HttpServletRequest request, @RequestParam(required = true) Long id) {
		try {
//            String CustomerInfoById = String.valueOf(customerService.getCustomerById(id));

			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId) || "2".equals(roleId)) { // admin and user

					return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in getCustomerById customer api: ", e);
			return new ResponseEntity<>("Error in getCustomerById customer api..!! " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<?> getAllCustomers(HttpServletRequest request) {
		try {

			String token = request.getHeader("Authorization");
			if ("SUCCESS".equalsIgnoreCase(jwtTokenUtil.authentication(token))) {

				String roleId = jwtTokenUtil.getIdFromToken(token);

				if ("1".equals(roleId)) {

					return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);

				} else {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN); // not have permission
				}
			} else {
				return new ResponseEntity<>("Invalid token or authentication failed", HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			log.error("Error in getAllCustomers customer api: ", e);
			return new ResponseEntity<>("Error in getAllCustomers customer api..!! " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// for token generation api
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String phoneNumber, @RequestParam Integer roleId) {

		Optional<Customer> cust = customerRepository.findByPhoneNumberAndRoleId(phoneNumber, roleId);
		if (cust.isEmpty()) {
			return ResponseEntity.status(401).body("Invalid phone number or role ID");
		}
		Customer customer = cust.get();

		CustomerDto customerDto = new CustomerDto();
		customerDto.setPhoneNumber(customer.getPhoneNumber());
		customerDto.setRoleId(customer.getRoleId());

		// Generate JWT Token
		JwtResponse jwtResponse = jwtTokenUtil.generateToken(customerDto);

		return ResponseEntity.ok(jwtResponse);
	}
}
