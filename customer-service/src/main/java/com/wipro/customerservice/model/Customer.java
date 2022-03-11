package com.wipro.customerservice.model;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String customerName;

    private String customerEmail;

    @OneToOne(targetEntity = CustomerBillingAddress.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address")
    private CustomerBillingAddress customerBillingAddress;

    @OneToOne(targetEntity = CustomerShippingAddress.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address")
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
