package com.example.composite_service.dto;

import javax.persistence.*;
import java.util.List;

public class Cart {

    private int cartId;

    private List<LineItem> lineItems;

    
	public Cart() {
		super();
	}

	public Cart(List<LineItem> lineItems) {
		super();
		this.lineItems = lineItems;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	@Override
	public String toString() {
		return "Cart [orderId=" + cartId + ", lineItems=" + lineItems + "]";
	}
	
	
    
    
}
