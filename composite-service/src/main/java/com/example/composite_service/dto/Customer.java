package com.example.composite_service.dto;

import javax.persistence.*;
import java.util.List;

public class Customer {

    private int id;

    private String customerName;

    private String customerEmail;

    private List<CustomerAddress> customerBillingAddress;

    @OneToMany(targetEntity = CustomerAddress.class,cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id",name = "address")
    private List<CustomerAddress> customerShippingAddress;

	public Customer() {
		super();
	}

	public Customer(String customerName, String customerEmail, List<CustomerAddress> customerBillingAddress,
			List<CustomerAddress> customerShippingAddress) {
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

	public List<CustomerAddress> getCustomerBillingAddress() {
		return customerBillingAddress;
	}

	public void setCustomerBillingAddress(List<CustomerAddress> customerBillingAddress) {
		this.customerBillingAddress = customerBillingAddress;
	}

	public List<CustomerAddress> getCustomerShippingAddress() {
		return customerShippingAddress;
	}

	public void setCustomerShippingAddress(List<CustomerAddress> customerShippingAddress) {
		this.customerShippingAddress = customerShippingAddress;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", customerName=" + customerName + ", customerEmail=" + customerEmail
				+ ", customerBillingAddress=" + customerBillingAddress + ", customerShippingAddress="
				+ customerShippingAddress + "]";
	}
    
    
}
