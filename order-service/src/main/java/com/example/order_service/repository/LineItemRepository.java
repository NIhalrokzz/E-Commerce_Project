package com.example.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.order_service.model.LineItem;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem,Integer> {
}
