package com.restauracja.restauracjaapi;

import com.restauracja.restauracjaapi.model.MenuCategory;
import com.restauracja.restauracjaapi.model.MenuItem;
import com.restauracja.restauracjaapi.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    void setUp() {
        menuItemRepository.deleteAll();
    }

    @Test
    void shouldReturn404WhenMenuItemNotFound() throws Exception {
        mockMvc.perform(get("/api/menu/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidMenuItem() throws Exception {
        String invalidJson = """
                {
                  "name": "",
                  "price": -5.00
                }
                """;

        mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateMenuItemSuccessfully() throws Exception {
        String validJson = """
                {
                  "name": "Margherita",
                  "description": "Pizza z sosem pomidorowym i serem",
                  "category": "MAIN",
                  "price": 32.00,
                  "available": true
                }
                """;

        mockMvc.perform(post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        MenuItem item = new MenuItem(
                "Margherita",
                "Pizza z sosem pomidorowym i serem",
                MenuCategory.MAIN,
                new BigDecimal("32.00"),
                true
        );

        MenuItem savedItem = menuItemRepository.save(item);

        mockMvc.perform(delete("/api/menu/" + savedItem.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldExportCsvWithCorrectContentType() throws Exception {
        mockMvc.perform(get("/api/orders/export/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("text/csv")));
    }
}