package com.restauracja.restauracjaapi.repository;

import com.restauracja.restauracjaapi.model.CustomerOrder;
import com.restauracja.restauracjaapi.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByDiningTableId(Long tableId);
    List<CustomerOrder> findByStatus(OrderStatus status);
    List<CustomerOrder> findByWaiterId(Long id);
}
