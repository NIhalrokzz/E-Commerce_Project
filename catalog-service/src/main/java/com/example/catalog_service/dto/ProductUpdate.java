package com.example.catalog_service.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ProductUpdate {

    private String productName;
    private String productDescription;
    private BigInteger productPrice;
    
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public BigInteger getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigInteger productPrice) {
		this.productPrice = productPrice;
	}
    
    
}
