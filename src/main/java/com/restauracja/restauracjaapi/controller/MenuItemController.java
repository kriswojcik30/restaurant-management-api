package com.restauracja.restauracjaapi.controller;

import com.restauracja.restauracjaapi.model.MenuCategory;
import com.restauracja.restauracjaapi.model.MenuItem;
import com.restauracja.restauracjaapi.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<MenuItem> create(@RequestBody @Valid MenuItem item) {
        MenuItem createdItem = menuItemService.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAll() {
        List<MenuItem> items = menuItemService.getAll();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getById(@PathVariable Long id) {
        MenuItem item = menuItemService.getById(id);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailable() {
        List<MenuItem> items = menuItemService.getAvailable();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getByCategory(@PathVariable String category) {
        MenuCategory menuCategory = MenuCategory.valueOf(category.toUpperCase());
        List<MenuItem> items = menuItemService.getByCategory(menuCategory);
        return ResponseEntity.ok(items);
    }
}