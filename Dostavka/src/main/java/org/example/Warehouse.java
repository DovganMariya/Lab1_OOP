package org.example;

import java.util.*;

// Класс для представления склада
class Warehouse {
    private final Coordinates coordinates;
    private final Map<String, Product> products;
    private final Map<String, Integer> stock;
    private final List<Worker> workers;
    private final Queue<Order> pendingOrders;
    private final Queue<Order> pickedOrders;

    public Warehouse(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.products = new HashMap<>();
        this.stock = new HashMap<>();
        this.workers = new ArrayList<>();
        this.pendingOrders = new LinkedList<>();
        this.pickedOrders = new LinkedList<>();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    public void addToStock(Product product, int quantity) {
        products.put(product.getName(), product);
        stock.put(product.getName(), stock.getOrDefault(product.getName(), 0) + quantity);
    }

    public int getStockQuantity(String productName) {
        return stock.getOrDefault(productName, 0);
    }

    public void orderFromSupplier(Supplier supplier, String productName, int quantity) {
        System.out.println("Склад заказывает у поставщика " + supplier.getName() +
                ": " + productName + " (" + quantity + " ед.)");
        supplier.receiveOrder(productName, quantity, this);
    }

    public void processOrder(Order order) {
        System.out.println("Склад обрабатывает заказ #" + order.getId());

        // Проверяем наличие товаров на складе
        Map<String, Integer> orderItems = new HashMap<>(order.getItems());
        for (Iterator<Map.Entry<String, Integer>> it = orderItems.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            String productName = entry.getKey();
            int quantity = entry.getValue();

            int availableQuantity = stock.getOrDefault(productName, 0);
            if (availableQuantity < quantity) {
                if (availableQuantity == 0) {
                    System.out.println("Товар " + productName + " отсутствует на складе");
                    it.remove();
                } else {
                    System.out.println("Недостаточно товара " + productName + " на складе. " +
                            "Доступно: " + availableQuantity + ", требуется: " + quantity);
                    entry.setValue(availableQuantity);
                }
            }
        }

        // Обновляем заказ и уменьшаем количество товаров на складе
        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();

            stock.put(productName, stock.get(productName) - quantity);
        }

        order.setStatus(OrderStatus.PROCESSING);
        pendingOrders.add(order);

        // Назначаем сборщика для заказа
        assignOrderToPicker();
    }

    public void assignOrderToPicker() {
        if (pendingOrders.isEmpty()) {
            return;
        }

        // Ищем свободного сборщика
        for (Worker worker : workers) {
            if (worker instanceof Picker && worker.isOnShift() && !((Picker) worker).isBusy()) {
                Picker picker = (Picker) worker;
                Order order = pendingOrders.poll();
                if (order != null) {
                    picker.assignOrder(order);
                    return;
                }
            }
        }
    }

    public void assignOrderToPicker(Picker picker) {
        if (pendingOrders.isEmpty() || !picker.isOnShift() || picker.isBusy()) {
            return;
        }

        Order order = pendingOrders.poll();
        if (order != null) {
            picker.assignOrder(order);
        }
    }

    public void orderPicked(Order order) {
        pickedOrders.add(order);
        assignOrderToCourier();
    }

    public void assignOrderToCourier() {
        if (pickedOrders.isEmpty()) {
            return;
        }

        // Ищем свободного курьера
        for (Worker worker : workers) {
            if (worker instanceof Courier && worker.isOnShift() &&
                    !((Courier) worker).isBusy() && !((Courier) worker).isReturningToWarehouse()) {
                Courier courier = (Courier) worker;
                Order order = pickedOrders.poll();
                if (order != null) {
                    courier.assignOrder(order);
                    return;
                }
            }
        }
    }

    public void assignOrderToCourier(Courier courier) {
        if (pickedOrders.isEmpty() || !courier.isOnShift() || courier.isBusy() || courier.isReturningToWarehouse()) {
            return;
        }

        Order order = pickedOrders.poll();
        if (order != null) {
            courier.assignOrder(order);
        }
    }

    public void assignCourierToOrder(Order order) {
        pickedOrders.add(order);
        assignOrderToCourier();
    }

    public double calculateDeliveryTime(Coordinates userCoordinates) {
        // 1 минута на выход со склада + время в пути + 1 минута на выдачу
        double distanceTime = (coordinates.distanceTo(userCoordinates) * 30) / 60; // в минутах
        return 1 + distanceTime + 1; // общее время в минутах
    }

    public void printStock() {
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " ед.");
        }
    }
}