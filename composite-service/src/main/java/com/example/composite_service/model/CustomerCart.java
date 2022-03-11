package com.example.composite_service.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_cart")
public class CustomerCart {

    @Id
    private int customerId;
    private int cartId;

    public CustomerCart(){
    }

    public CustomerCart(int customerId,int cartId) {
        this.customerId = customerId;
        this.cartId = cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "customerId=" + customerId +
                ", cartId=" + cartId +
                '}';
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
