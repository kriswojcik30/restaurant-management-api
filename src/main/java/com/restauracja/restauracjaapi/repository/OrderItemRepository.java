package com.restauracja.restauracjaapi.repository;

import com.restauracja.restauracjaapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByCustomerOrderId(Long orderId);
}
