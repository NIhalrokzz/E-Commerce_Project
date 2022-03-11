package com.example.catalog_service.dto;

import lombok.Data;

@Data
public class ProductInventory {
	
    private String productName;
    private int availableQuantity;
    
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
    
    
}
