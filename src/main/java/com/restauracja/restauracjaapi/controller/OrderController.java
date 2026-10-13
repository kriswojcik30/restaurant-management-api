package com.restauracja.restauracjaapi.controller;

import com.restauracja.restauracjaapi.model.CustomerOrder;
import com.restauracja.restauracjaapi.model.OrderStatus;
import com.restauracja.restauracjaapi.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody CreateOrderRequest request) {
        CustomerOrder order = orderService.createOrder(request.getTableId(), request.getWaiterId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getById(@PathVariable Long id) {
        CustomerOrder order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<Map<String, Object>> getTotal(@PathVariable Long id) {
        BigDecimal total = orderService.calculateTotal(id);

        return ResponseEntity.ok(Map.of(
                "orderId", id,
                "total", total
        ));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CustomerOrder> addItem(@PathVariable Long id,
                                                 @RequestBody AddItemRequest request) {
        CustomerOrder order = orderService.addItem(id, request.getMenuItemId(), request.getQuantity());
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerOrder> updateStatus(@PathVariable Long id,
                                                      @RequestParam String status) {
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        CustomerOrder order = orderService.updateStatus(id, newStatus);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv() {
        byte[] csv = orderService.exportToCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orders.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    public static class CreateOrderRequest {
        private Long tableId;
        private Long waiterId;

        public Long getTableId() {
            return tableId;
        }

        public void setTableId(Long tableId) {
            this.tableId = tableId;
        }

        public Long getWaiterId() {
            return waiterId;
        }

        public void setWaiterId(Long waiterId) {
            this.waiterId = waiterId;
        }
    }

    public static class AddItemRequest {
        private Long menuItemId;
        private int quantity;

        public Long getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(Long menuItemId) {
            this.menuItemId = menuItemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}