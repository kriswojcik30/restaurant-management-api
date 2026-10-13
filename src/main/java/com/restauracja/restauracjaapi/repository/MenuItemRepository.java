package com.restauracja.restauracjaapi.repository;

import com.restauracja.restauracjaapi.model.MenuCategory;
import com.restauracja.restauracjaapi.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategory(MenuCategory category);
    List<MenuItem> findByAvailableTrue();
}
