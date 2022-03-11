package com.example.cart_service.repository;

import com.example.cart_service.model.LineItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem,Integer> {
}
