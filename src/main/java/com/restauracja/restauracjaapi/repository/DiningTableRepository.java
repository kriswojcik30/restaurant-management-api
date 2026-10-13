package com.restauracja.restauracjaapi.repository;

import com.restauracja.restauracjaapi.model.DiningTable;
import com.restauracja.restauracjaapi.model.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {

    List<DiningTable> findByStatus(TableStatus status);
    Optional<DiningTable> findByTableNumber(Integer tableNumber);
}
