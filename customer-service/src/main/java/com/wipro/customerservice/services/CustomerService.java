package com.wipro.customerservice.services;

import java.util.Optional;

import com.wipro.customerservice.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.wipro.customerservice.dto.CustomerInfo;
import com.wipro.customerservice.model.Customer;
import com.wipro.customerservice.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer(Customer customer){
    	System.out.println("This is called");
        return customerRepository.save(customer);
    }

    public boolean deleteCustomer(int customerID){

        if(customerRepository.findById(customerID).isPresent()){
            customerRepository.delete(customerRepository.findById(customerID).get());
        }else{
            return false;
        }
        return true;
    }

    public Customer updateCustomer(int customerID, CustomerInfo customerInfo) throws CustomException {
        if(customerRepository.findById(customerID).isPresent()){
            Customer customer = customerRepository.findById(customerID).get();
            customer.setCustomerName(customerInfo.getCustomerName());
            customer.setCustomerEmail(customerInfo.getCustomerEmail());

            customerRepository.save(customer);
            return customer;
        }else{
            throw new CustomException("Unable to find the customer", HttpStatus.NOT_FOUND);
        }
    }

    public Customer searchCustomer(int customerID) throws CustomException {
        if(customerRepository.findById(customerID).isPresent()){
            return customerRepository.findById(customerID).get();
        }else{
            throw new CustomException("Unable to find the customer", HttpStatus.NOT_FOUND);
        }
    }
    
    public Customer searchByEmail(String email) throws CustomException {
    	Optional<Customer> customer = customerRepository.findByCustomerEmail(email);
    	
    	if(customer.isPresent()) {
    		return customer.get();
    	}else{
            throw new CustomException("Unable to find the customer", HttpStatus.NOT_FOUND);
        }
    }
}
