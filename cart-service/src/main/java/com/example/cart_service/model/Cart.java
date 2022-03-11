package com.example.cart_service.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartId;

    @OneToMany(targetEntity = LineItem.class, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "cartId", name = "cart_line")
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
