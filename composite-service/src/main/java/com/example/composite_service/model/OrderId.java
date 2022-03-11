package com.example.composite_service.model;

import javax.persistence.*;

@Entity
@Table(name = "order_id")
public class OrderId {

    @Id
    private int orderId;

	public OrderId() {
	}

	public OrderId(int orderId) {
		super();
	}


	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + "]";
	}
}
