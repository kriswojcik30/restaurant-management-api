package com.restauracja.restauracjaapi.controller;

import com.restauracja.restauracjaapi.model.Waiter;
import com.restauracja.restauracjaapi.service.WaiterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiters")
public class WaiterController {

    private final WaiterService waiterService;

    public WaiterController(WaiterService waiterService) {
        this.waiterService = waiterService;
    }

    @PostMapping
    public ResponseEntity<Waiter> create(@RequestBody @Valid Waiter waiter) {
        Waiter createdWaiter = waiterService.create(waiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWaiter);
    }

    @GetMapping
    public ResponseEntity<List<Waiter>> getAll() {
        List<Waiter> waiters = waiterService.getAll();
        return ResponseEntity.ok(waiters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Waiter> getById(@PathVariable Long id) {
        Waiter waiter = waiterService.getById(id);
        return ResponseEntity.ok(waiter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waiterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}