package com.wipro.customerservice.model;

import javax.persistence.*;

@Entity
@Table(name = "customer_shipping_address")
public class CustomerShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Long DoorNo;
    private String streetName;
    private String Layout;
    private String city;
    private int pincode;
    
	public CustomerShippingAddress() {
		super();
	}

	public CustomerShippingAddress(Long doorNo, String streetName, String layout, String city, int pincode) {
		super();
		this.DoorNo = doorNo;
		this.streetName = streetName;
		this.Layout = layout;
		this.city = city;
		this.pincode = pincode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getDoorNo() {
		return DoorNo;
	}

	public void setDoorNo(Long doorNo) {
		DoorNo = doorNo;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getLayout() {
		return Layout;
	}

	public void setLayout(String layout) {
		Layout = layout;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "CustomerAddress [id=" + id + ", DoorNo=" + DoorNo + ", streetName=" + streetName + ", Layout=" + Layout
				+ ", city=" + city + ", pincode=" + pincode + "]";
	}
    
}
