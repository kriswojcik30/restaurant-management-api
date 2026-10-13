package com.restauracja.restauracjaapi.service;

import com.restauracja.restauracjaapi.model.*;
import com.restauracja.restauracjaapi.repository.*;
import org.springframework.stereotype.Service;
import com.restauracja.restauracjaapi.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final CustomerOrderRepository customerOrderRepository;
    private final DiningTableRepository diningTableRepository;
    private final WaiterRepository waiterRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CustomerOrderRepository customerOrderRepository,
                        DiningTableRepository diningTableRepository,
                        WaiterRepository waiterRepository,
                        MenuItemRepository menuItemRepository,
                        OrderItemRepository orderItemRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.diningTableRepository = diningTableRepository;
        this.waiterRepository = waiterRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public CustomerOrder createOrder(Long tableId, Long waiterId) {
        DiningTable table = diningTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono stolika o id: " + tableId));

        Waiter waiter = waiterRepository.findById(waiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono kelnera o id: " + waiterId));

        CustomerOrder order = new CustomerOrder();
        order.setDiningTable(table);
        order.setWaiter(waiter);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.OPEN);

        table.setStatus(TableStatus.OCCUPIED);
        diningTableRepository.save(table);

        return customerOrderRepository.save(order);
    }

    public CustomerOrder addItem(Long orderId, Long menuItemId, int quantity) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zamówienia o id: " + orderId));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono dania o id: " + menuItemId));

        OrderItem orderItem = new OrderItem();
        orderItem.setCustomerOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(menuItem.getPrice());

        orderItemRepository.save(orderItem);

        order.getItems().add(orderItem);

        return customerOrderRepository.save(order);
    }

    public BigDecimal calculateTotal(Long orderId) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zamówienia o id: " + orderId));

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }

        return total;
    }

    public CustomerOrder updateStatus(Long orderId, OrderStatus newStatus) {
        CustomerOrder order = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zamówienia o id: " + orderId));

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.PAID) {
            DiningTable table = order.getDiningTable();
            table.setStatus(TableStatus.FREE);
            diningTableRepository.save(table);
        }

        return customerOrderRepository.save(order);
    }

    public byte[] exportToCsv() {
        StringBuilder csv = new StringBuilder();

        csv.append("ID,TableNumber,WaiterLastName,OrderTime,Status,Total\n");

        List<CustomerOrder> orders = customerOrderRepository.findAll();

        for (CustomerOrder order : orders) {
            BigDecimal total = calculateTotal(order.getId());

            csv.append(order.getId()).append(",");
            csv.append(order.getDiningTable().getTableNumber()).append(",");
            csv.append(order.getWaiter().getLastName()).append(",");
            csv.append(order.getOrderTime()).append(",");
            csv.append(order.getStatus()).append(",");
            csv.append(total).append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public CustomerOrder getById(Long id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zamówienia o id: " + id));
    }
}