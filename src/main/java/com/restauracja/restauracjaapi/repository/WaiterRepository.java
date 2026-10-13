package com.restauracja.restauracjaapi.repository;

import com.restauracja.restauracjaapi.model.Waiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaiterRepository extends JpaRepository<Waiter, Long> {
    List<Waiter> findByActiveTrue();
}
