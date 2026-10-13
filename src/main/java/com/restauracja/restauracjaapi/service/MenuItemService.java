package com.restauracja.restauracjaapi.service;

import com.restauracja.restauracjaapi.exception.ResourceNotFoundException;
import com.restauracja.restauracjaapi.model.MenuCategory;
import com.restauracja.restauracjaapi.model.MenuItem;
import com.restauracja.restauracjaapi.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository){
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAll(){
        return menuItemRepository.findAll();
    }

    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono dania o id: " + id));
    }

    public MenuItem create(MenuItem item) {
        return menuItemRepository.save(item);
    }

    public void delete(Long id) {
        MenuItem item = getById(id);
        menuItemRepository.delete(item);
    }

    public List<MenuItem> getAvailable() {
        return menuItemRepository.findByAvailableTrue();
    }

    public List<MenuItem> getByCategory(MenuCategory category){
        return menuItemRepository.findByCategory(category);
    }
}
