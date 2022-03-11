package com.example.composite_service.repository;

import com.example.composite_service.model.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderIdRepository extends JpaRepository<OrderId, Integer> {
}
