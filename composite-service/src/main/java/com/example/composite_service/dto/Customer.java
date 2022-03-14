package com.example.composite_service.dto;

import javax.persistence.*;
import java.util.List;

public class Customer {

    private int id;

    private String customerName;

    private String customerEmail;

    private CustomerBillingAddress customerBillingAddress;

    private CustomerShippingAddress customerShippingAddress;

	public Customer() {
		super();
	}

	public Customer(String customerName, String customerEmail, CustomerBillingAddress customerBillingAddress,
			CustomerShippingAddress customerShippingAddress) {
		super();
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerBillingAddress = customerBillingAddress;
		this.customerShippingAddress = customerShippingAddress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public CustomerBillingAddress getCustomerBillingAddress() {
		return customerBillingAddress;
	}

	public void setCustomerBillingAddress(CustomerBillingAddress customerBillingAddress) {
		this.customerBillingAddress = customerBillingAddress;
	}

	public CustomerShippingAddress getCustomerShippingAddress() {
		return customerShippingAddress;
	}

	public void setCustomerShippingAddress(CustomerShippingAddress customerShippingAddress) {
		this.customerShippingAddress = customerShippingAddress;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerName=" + customerName + ", customerEmail=" + customerEmail
				+ ", customerBillingAddress=" + customerBillingAddress + ", customerShippingAddress="
				+ customerShippingAddress + "]";
	}
    
    
}
