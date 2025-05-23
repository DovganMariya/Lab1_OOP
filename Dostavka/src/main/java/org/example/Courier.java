package org.example;

import java.util.concurrent.CompletableFuture;

// Класс для представления курьера
class Courier extends Worker {
    private Order currentOrder;
    private boolean busy;
    private boolean returningToWarehouse;

    public Courier(String firstName, String lastName) {
        super(firstName, lastName);
        this.busy = false;
        this.returningToWarehouse = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public boolean isReturningToWarehouse() {
        return returningToWarehouse;
    }

    public void assignOrder(Order order) {
        if (!onShift) {
            throw new IllegalStateException("Курьер не на смене");
        }
        if (busy) {
            throw new IllegalStateException("Курьер уже занят другим заказом");
        }
        this.currentOrder = order;
        this.busy = true;
        order.assignCourier(this);
        System.out.println(getFullName() + " назначен на доставку заказа #" + order.getId());

        // Запускаем доставку заказа в отдельном потоке
        CompletableFuture.runAsync(this::deliverOrder);
    }

    private void deliverOrder() {
        if (currentOrder == null) {
            return;
        }

        System.out.println(getFullName() + " начал доставку заказа #" + currentOrder.getId());

        User user = currentOrder.getUser();
        double deliveryTimeMinutes = warehouse.calculateDeliveryTime(user.getCoordinates());
        long deliveryTimeMillis = (long) (deliveryTimeMinutes * 60 * 1000);

        try {
            // Симулируем время доставки
            Thread.sleep(deliveryTimeMillis / 1000); // Ускоряем для демонстрации
            /// Thread.sleep(deliveryTimeMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        currentOrder.markAsDelivered();
        System.out.println(getFullName() + " доставил заказ #" + currentOrder.getId() +
                " пользователю " + user.getFullName());

        // Возвращаемся на склад
        returningToWarehouse = true;

        // Время возвращения на склад равно времени пути от пользователя до склада
        double returnTimeMinutes = (warehouse.getCoordinates().distanceTo(user.getCoordinates()) * 30) / 60;
        long returnTimeMillis = (long) (returnTimeMinutes * 60 * 1000);

        try {
            // Симулируем время возвращения
            Thread.sleep(returnTimeMillis / 1000); // Ускоряем для демонстрации
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.busy = false;
        this.returningToWarehouse = false;
        this.currentOrder = null;

        System.out.println(getFullName() + " вернулся на склад");

        // Проверяем, есть ли еще заказы для доставки
        processTask();
    }

    @Override
    void processTask() {
        if (onShift && !busy && !returningToWarehouse) {
            warehouse.assignOrderToCourier(this);
        }
    }
}