package com.wipro.customerservice.controller;

import com.wipro.customerservice.dto.CustomerInfo;
import com.wipro.customerservice.dto.CustomerRequest;
import com.wipro.customerservice.exception.CustomException;
import com.wipro.customerservice.model.Customer;
import com.wipro.customerservice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer")
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerRequest customerRequest) throws CustomException {

        if(customerRequest.getCustomer().getCustomerName().length() == 0 || customerRequest.getCustomer().getCustomerEmail().length() == 0 || customerRequest.getCustomer().getCustomerBillingAddress() == null || customerRequest.getCustomer().getCustomerShippingAddress() == null){
            throw new CustomException("Invalid data", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerService.addCustomer(customerRequest.getCustomer());
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @DeleteMapping("/customer/{customerID}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerID") int customerID) throws CustomException {

        if(customerID < 0){
            throw new CustomException("Customer ID cannot be negative", HttpStatus.BAD_REQUEST);
        }

        if(customerService.deleteCustomer(customerID)){
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("SOMETHING WRONG", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/customer/{customerID}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerID") int customerID, @RequestBody CustomerInfo customerInfo) throws CustomException {

        if(customerID < 0){
            throw new CustomException("Customer ID cannot be negative", HttpStatus.BAD_REQUEST);
        }

        if(customerInfo.getCustomerName().length() == 0 || customerInfo.getCustomerEmail().length() == 0){
            throw new CustomException("Please provide valid inputs", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerService.updateCustomer(customerID, customerInfo);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerID}")
    public ResponseEntity<Customer> searchCustomer(@PathVariable("customerID") int customerID) throws CustomException {

        if(customerID < 0){
            throw new CustomException("Customer ID cannot be negative", HttpStatus.BAD_REQUEST);
        }

        Customer searchCustomer = customerService.searchCustomer(customerID);
        return new ResponseEntity<>(searchCustomer, HttpStatus.OK);
    }
    
    @GetMapping("/customerByEmail/{customerEmail}")
    public ResponseEntity<Customer> searchCustomer(@PathVariable("customerEmail") String email) throws CustomException {
        if(email.length() == 0){
            throw new CustomException("Please Provide valid input", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerService.searchByEmail(email);
        return new ResponseEntity<Customer>(customer, HttpStatus.FOUND);
    }

}
