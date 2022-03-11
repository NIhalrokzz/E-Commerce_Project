package com.example.order_service.dto;

import java.util.List;

import com.example.order_service.model.LineItem;

public class OrderUpdate {
    private List<LineItem> lineItems;

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
    
    
}
