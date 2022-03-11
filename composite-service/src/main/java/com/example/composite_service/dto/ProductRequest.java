package com.example.composite_service.dto;

import java.math.BigInteger;

public class ProductRequest {

    private String productName;
    private String productDescription;
    private BigInteger productPrice;
    private int quantity;

    public ProductRequest() {
    }

    public ProductRequest(String productName, String productDescription, BigInteger productPrice, int quantity) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                '}';
    }
}
