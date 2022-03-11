package com.example.composite_service.dto;


import java.util.List;

public class CartUpdate {
    private List<LineItem> lineItems;

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
    
    
}
