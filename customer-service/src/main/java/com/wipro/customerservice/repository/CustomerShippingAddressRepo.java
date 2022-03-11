package com.wipro.customerservice.repository;

import com.wipro.customerservice.model.CustomerShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerShippingAddressRepo extends JpaRepository<CustomerShippingAddress, Integer> {
}
