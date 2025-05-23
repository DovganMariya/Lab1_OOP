package org.example;

import java.util.concurrent.CompletableFuture;

class Picker extends Worker {
    private Order currentOrder;
    private boolean busy;

    public Picker(String firstName, String lastName) {
        super(firstName, lastName);
        this.busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void assignOrder(Order order) {
        if (!onShift) {
            throw new IllegalStateException("Сборщик не на смене");
        }
        if (busy) {
            throw new IllegalStateException("Сборщик уже занят другим заказом");
        }
        this.currentOrder = order;
        this.busy = true;
        order.assignPicker(this);
        System.out.println(getFullName() + " назначен на сборку заказа #" + order.getId());

        // Запускаем сборку заказа в отдельном потоке
        CompletableFuture.runAsync(this::pickOrder);
    }

    private void pickOrder() {
        if (currentOrder == null) {
            return;
        }

        System.out.println(getFullName() + " начал сборку заказа #" + currentOrder.getId());

        // Время сборки: 45 секунд на каждый товар
        int totalItems = currentOrder.getItems().values().stream().mapToInt(Integer::intValue).sum();
        long pickingTimeMillis = totalItems * 45 * 1000L;

        try {
            // Симулируем время сборки
            Thread.sleep(pickingTimeMillis / 1000); // Ускоряем для демонстрации
            /// Thread.sleep(pickingTimeMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        currentOrder.markAsPicked();
        System.out.println(getFullName() + " завершил сборку заказа #" + currentOrder.getId());

        // Назначаем курьера для доставки
        warehouse.assignCourierToOrder(currentOrder);

        this.busy = false;
        this.currentOrder = null;

        // Проверяем, есть ли еще заказы для сборки
        processTask();
    }

    @Override
    void processTask() {
        if (onShift && !busy) {
            warehouse.assignOrderToPicker(this);
        }
    }
}