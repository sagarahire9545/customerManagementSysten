package com.custManagent.serviceImpl;

import com.custManagent.dto.CustomerDto;
import com.custManagent.entity.Customer;
import com.custManagent.mapper.CustomerMapper;
import com.custManagent.repository.CustomerRepository;
import com.custManagent.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomerServiceIml implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Override
    public Customer createCustomer(String name, String email, String phoneNumber) {

        Customer customer = new Customer();

        try {
            if (name != null && !name.isEmpty()) {
                customer.setName(name);
            }
            if (email != null && !email.isEmpty()) {
                customer.setEmail(email);
            }
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                customer.setPhoneNumber(phoneNumber);
            }
            customer.setRoleId(1);
            customerRepository.save(customer);

        } catch(Exception e){
            log.error("Error Getting in Customer Saving Time. " , e.getMessage());
            return null;

        }

        return customer;
    }

    @Override
    public Customer updateCustomerById(Long id, String name, String email, String phoneNumber) {

        if (id == null) {
            log.error("Customer ID cannot be null.");
            return null;
        }

        //to get customer using id
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            log.info("Customer not found with ID: {}", id);
            return null;
        }

        if (name != null && !name.isEmpty()) {
            customer.setName(name);
        }
        if (email != null && !email.isEmpty()) {
            customer.setEmail(email);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            customer.setPhoneNumber(phoneNumber);
        }

        try {
            customerRepository.save(customer);
            log.info("Customer updated successfully: {}", customer);
        } catch (Exception e) {
            log.error("Error occurred while updating customer with ID {}: {}", id, e.getMessage());
            return null;
        }
        return customer;
    }

    @Override
    public String deleteCustomerById(Long id) {
        if (id == null) {
            log.error("Customer ID cannot be null.");
            return "Customer ID cannot be null. Please Provide Id. !!";
        }
        try {
            Customer customer = customerRepository.findById(id).orElse(null);

            if (customer != null) {
                customerRepository.deleteById(id);
                log.info("Customer deleted successfully with ID: {}", id);
                return "Customer deleted successfully";
            }else{

                log.info("Customer not found with ID: {}", id);
                return "Customer not found for this id.";
            }

        } catch (Exception e) {
            log.error("Error Getting While Customer Delete Time with ID {}: {}", id, e.getMessage());
            return "Error Getting While Customer Delete Time.";
        }
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        if (id == null) {
            log.error("Customer ID cannot be null.");
            return null;
        }
        try {
            Customer customer = customerRepository.findById(id).orElse(null);
            if (customer == null) {
                log.info("Customer not found with ID: {}", id);
                return null;
            }
            //using mapper
            CustomerDto customerDto = customerMapper.mapEntityToDto(customer);
            
            log.info("Successfully retrieved customer with ID: {}", id);
            return customerDto;

        } catch (Exception e) {
            log.error("Error getting while getting customer with ID {}: {}", id, e.getMessage());
            return null;
        }
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        //i need dto return
        try {
            List<Customer> customers = customerRepository.findAll();

            List<CustomerDto> customerDtos = new ArrayList<>();

            if (!customers.isEmpty()) {
                for (Customer customer : customers) {
                    CustomerDto customerDto = customerMapper.mapEntityToDto(customer);
                    customerDtos.add(customerDto);
                }
            } else {
                log.info("No customers found.");
            }

            log.info("Successfully retrieved all customers.");
            return customerDtos;

        } catch (Exception e) {
            log.error("Error occurred while retrieving all customers: {}", e.getMessage(), e);
            return null;
        }
    }
}
