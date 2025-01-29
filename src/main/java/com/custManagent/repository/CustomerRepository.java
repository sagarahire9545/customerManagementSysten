package com.custManagent.repository;

import com.custManagent.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	//for get contact for login api
//	Optional<Customer> findByPhoneNumberAndRoleId(String phoneNumber, Integer  roleId);

	Optional<Customer> findByPhoneNumberAndRoleId(String phoneNumber, Integer roleId);

}
