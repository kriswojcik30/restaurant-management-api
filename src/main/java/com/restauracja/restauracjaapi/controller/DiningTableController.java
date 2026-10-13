package com.restauracja.restauracjaapi.controller;

import com.restauracja.restauracjaapi.model.DiningTable;
import com.restauracja.restauracjaapi.model.TableStatus;
import com.restauracja.restauracjaapi.service.DiningTableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class DiningTableController {

    private final DiningTableService diningTableService;

    public DiningTableController(DiningTableService diningTableService) {
        this.diningTableService = diningTableService;
    }

    @PostMapping
    public ResponseEntity<DiningTable> create(@RequestBody @Valid DiningTable table) {
        DiningTable createdTable = diningTableService.create(table);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
    }

    @GetMapping
    public ResponseEntity<List<DiningTable>> getAll() {
        List<DiningTable> tables = diningTableService.getAll();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/free")
    public ResponseEntity<List<DiningTable>> getFree() {
        List<DiningTable> freeTables = diningTableService.getFree();
        return ResponseEntity.ok(freeTables);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DiningTable> updateStatus(@PathVariable Long id,
                                                    @RequestParam String status) {
        TableStatus newStatus = TableStatus.valueOf(status.toUpperCase());
        DiningTable updatedTable = diningTableService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updatedTable);
    }
}