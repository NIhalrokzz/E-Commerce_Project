package com.wipro.customerservice.services;

import com.wipro.customerservice.model.Customer;

import com.wipro.customerservice.model.CustomerBillingAddress;
import com.wipro.customerservice.repository.CustomerBillingAddressRepo;
import com.wipro.customerservice.repository.CustomerRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class CustomerAddressService {

    @Autowired
    private CustomerBillingAddressRepo customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer addcustomerAddress(int customerID, CustomerBillingAddress customerAddress){
        if(customerRepository.findById(customerID).isPresent()){
            Customer customer = customerRepository.findById(customerID).get();
            customer.setCustomerBillingAddress(customerAddress);

            return customer;
        }
        return null;

    }

    public void deleteCustomerAddress(int customerID,int addressId){
    	Optional<CustomerBillingAddress> address = customerAddressRepository.findById(addressId);
        address.ifPresent(customerBillingAddress -> customerAddressRepository.delete(customerBillingAddress));
    }

    public CustomerBillingAddress updateCustomerAddress(int addressId, CustomerBillingAddress customerAddress){
    	Optional<CustomerBillingAddress> address = customerAddressRepository.findById(addressId);
    	
    	if(address.isPresent()) {
    		address.get().setDoorNo(customerAddress.getDoorNo());
    		address.get().setCity(customerAddress.getCity());
    		address.get().setLayout(customerAddress.getLayout());
    		address.get().setPincode(customerAddress.getPincode());
    		address.get().setStreetName(customerAddress.getStreetName());
    	}
    	
    	return customerAddressRepository.save(address.get());
    }

    public CustomerBillingAddress searchCustomerAddress(int addressId){
    	return customerAddressRepository.findById(addressId).get();
    }
}
