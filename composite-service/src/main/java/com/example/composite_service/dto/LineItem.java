package com.example.composite_service.dto;

import javax.persistence.*;
import java.math.BigInteger;

public class LineItem {

    private int itemId;
    private int productId;
    private String productName;
    private int quantity;
    private BigInteger price;

	public LineItem() {
		super();
	}

	public LineItem(int productId, String productName, int quantity, BigInteger price) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigInteger getPrice() {
		return price;
	}

	public void setPrice(BigInteger price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "LineItem [itemId=" + itemId + ", productId=" + productId + ", productName=" + productName
				+ ", quantity=" + quantity + ", price=" + price + "]";
	}
	
}
