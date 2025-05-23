package org.example;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Order {
    private static int nextId = 1;

    private final int id;
    private final User user;
    private final Map<String, Integer> items;
    private OrderStatus status;
    private Picker assignedPicker;
    private Courier assignedCourier;
    private LocalDateTime createdAt;
    private LocalDateTime pickedAt;
    private LocalDateTime deliveredAt;

    public Order(User user) {
        this.id = nextId++;
        this.user = user;
        this.items = new HashMap<>();
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Map<String, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void addItem(String productName, int quantity) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Нельзя добавлять товары в заказ после его создания");
        }
        items.put(productName, items.getOrDefault(productName, 0) + quantity);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Picker getAssignedPicker() {
        return assignedPicker;
    }

    public void assignPicker(Picker picker) {
        this.assignedPicker = picker;
        this.status = OrderStatus.PICKING;
    }

    public Courier getAssignedCourier() {
        return assignedCourier;
    }

    public void assignCourier(Courier courier) {
        this.assignedCourier = courier;
        this.status = OrderStatus.DELIVERING;
    }

    public void markAsPicked() {
        this.status = OrderStatus.PICKED;
        this.pickedAt = LocalDateTime.now();
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPickedAt() {
        return pickedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    @Override
    public String toString() {
        return "Заказ #" + id + " (" + status + ")";
    }
}

enum OrderStatus {
    CREATED,     // Создан
    PROCESSING,  // Обрабатывается
    PICKING,     // Собирается
    PICKED,      // Собран
    DELIVERING,  // Доставляется
    DELIVERED,   // Доставлен
    CANCELLED    // Отменен
}
