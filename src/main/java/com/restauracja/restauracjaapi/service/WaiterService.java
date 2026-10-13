package com.restauracja.restauracjaapi.service;

import com.restauracja.restauracjaapi.exception.ResourceNotFoundException;
import com.restauracja.restauracjaapi.model.Waiter;
import com.restauracja.restauracjaapi.repository.WaiterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaiterService {

    private final WaiterRepository waiterRepository;

    public WaiterService(WaiterRepository waiterRepository) {
        this.waiterRepository = waiterRepository;
    }

    public List<Waiter> getAll() {
        return waiterRepository.findAll();
    }

    public Waiter getById(Long id) {
        return waiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono kelnera o id: " + id));
    }

    public Waiter create(Waiter waiter) {
        return waiterRepository.save(waiter);
    }

    public void delete(Long id) {
        Waiter waiter = getById(id);
        waiterRepository.delete(waiter);
    }
}