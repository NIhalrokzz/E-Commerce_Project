package com.example.composite_service.model;

import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class CustomerOrder {

    @Id
    private int customerId;

    @OneToMany(targetEntity= OrderId.class,cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "customerId", name = "customer_order")
    private List<OrderId> orderId;

    public CustomerOrder() {
    }

    public CustomerOrder(List<OrderId> orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<OrderId> getOrderId() {
        return orderId;
    }

    public void setOrderId(List<OrderId> orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "customerId=" + customerId +
                ", orderId=" + orderId +
                '}';
    }
}
