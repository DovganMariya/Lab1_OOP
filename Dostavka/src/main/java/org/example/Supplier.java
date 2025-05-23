package org.example;

import java.util.HashMap;
import java.util.Map;

class Supplier {
    private final String name;
    private final Map<String, Product> products;
    private final Map<String, Integer> stock;

    public Supplier(String name) {
        this.name = name;
        this.products = new HashMap<>();
        this.stock = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product) {
        products.put(product.getName(), product);
        stock.put(product.getName(), 1000); // Предположим, что у поставщика много товара
    }

    public Product getProduct(String productName) {
        return products.get(productName);
    }

    public int getStock(String productName) {
        return stock.getOrDefault(productName, 0);
    }

    public void receiveOrder(String productName, int quantity, Warehouse warehouse) {
        if (!products.containsKey(productName)) {
            System.out.println("Поставщик " + name + " не имеет товара: " + productName);
            return;
        }

        int availableQuantity = Math.min(quantity, stock.getOrDefault(productName, 0));
        stock.put(productName, stock.get(productName) - availableQuantity);

        System.out.println("Поставщик " + name + " получил заказ на " +
                availableQuantity + " ед. товара: " + productName);
        deliverToWarehouse(warehouse, productName, availableQuantity);
    }

    public void deliverToWarehouse(Warehouse warehouse, String productName, int quantity) {
        System.out.println("Поставщик " + name + " доставляет товары на склад");

        warehouse.addToStock(products.get(productName), quantity);
        System.out.println("Доставлено " + quantity + " ед. товара: " + productName);

        /*for (Map.Entry<String, Product> entry : products.entrySet()) {
            String productName = entry.getKey();
            Product product = entry.getValue();

            // Предположим, что поставщик доставляет случайное количество товара
            // int quantity = new Random().nextInt(50) + 50;
            warehouse.addToStock(product, quantity);

            System.out.println("Доставлено " + quantity + " ед. товара: " + productName);
        }*/
    }
}