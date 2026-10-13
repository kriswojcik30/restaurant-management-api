package com.restauracja.restauracjaapi.service;

import com.restauracja.restauracjaapi.exception.ResourceNotFoundException;
import com.restauracja.restauracjaapi.model.DiningTable;
import com.restauracja.restauracjaapi.model.TableStatus;
import com.restauracja.restauracjaapi.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiningTableService {

    private final DiningTableRepository diningTableRepository;

    public DiningTableService(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    public List<DiningTable> getAll() {
        return diningTableRepository.findAll();
    }

    public DiningTable getById(Long id) {
        return diningTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono stolika o id: " + id));
    }

    public DiningTable create(DiningTable table) {
        return diningTableRepository.save(table);
    }

    public List<DiningTable> getFree() {
        return diningTableRepository.findByStatus(TableStatus.FREE);
    }

    public DiningTable updateStatus(Long id, TableStatus newStatus) {
        DiningTable table = getById(id);
        table.setStatus(newStatus);
        return diningTableRepository.save(table);
    }
}