package com.example.composite_service.dto;

import javax.persistence.*;
import java.util.List;

public class Order {

    private int orderId;
    private List<LineItem> lineItems;

    
	public Order() {
		super();
	}

	public Order(List<LineItem> lineItems) {
		super();
		this.lineItems = lineItems;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", lineItems=" + lineItems + "]";
	}
	
	
    
    
}
