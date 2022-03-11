package com.wipro.customerservice.repository;

import com.wipro.customerservice.model.CustomerBillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBillingAddressRepo extends JpaRepository<CustomerBillingAddress, Integer> {
}
