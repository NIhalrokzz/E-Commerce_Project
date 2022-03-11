package com.example.cart_service.dto;

import com.example.cart_service.model.LineItem;

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
