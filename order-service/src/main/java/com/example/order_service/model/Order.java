package com.example.order_service.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    @OneToMany(targetEntity = LineItem.class, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "orderId", name = "order_line")
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
