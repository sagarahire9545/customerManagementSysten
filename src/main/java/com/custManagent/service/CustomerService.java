package com.custManagent.service;

import com.custManagent.dto.CustomerDto;
import com.custManagent.entity.Customer;

import java.util.List;

public interface CustomerService {

    //save customer details
    public Customer createCustomer(String name, String email, String phoneNumber) ;

    //update customer details
    public Customer updateCustomerById(Long id, String name, String email, String phoneNumber);

    //delete customer by id
    String deleteCustomerById(Long id);

    //get customer details using id
    public CustomerDto getCustomerById(Long id);

    //get all customers
    public List<CustomerDto> getAllCustomers();

}
