package org.example;

public class Main {
    public static void main(String[] args) {
        // Создаем склад
        Warehouse warehouse = new Warehouse(new Coordinates(50, 50));

        // Создаем поставщиков
        Supplier supplier1 = new Supplier("Поставщик продуктов");
        Supplier supplier2 = new Supplier("Поставщик электроники");

        // Добавляем товары в каталог поставщиков
        supplier1.addProduct(new Product("Хлеб", 50.0));
        supplier1.addProduct(new Product("Молоко", 80.0));
        supplier1.addProduct(new Product("Яблоки", 120.0));

        supplier2.addProduct(new Product("Наушники", 2000.0));
        supplier2.addProduct(new Product("Зарядка", 500.0));

        // Склад делает заказы поставщикам
        warehouse.orderFromSupplier(supplier1, "Хлеб", 100);
        warehouse.orderFromSupplier(supplier1, "Молоко", 50);
        warehouse.orderFromSupplier(supplier1, "Яблоки", 80);
        warehouse.orderFromSupplier(supplier2, "Наушники", 20);
        warehouse.orderFromSupplier(supplier2, "Зарядка", 30);

        // Поставщики доставляют товары на склад
        //supplier1.deliverToWarehouse(warehouse);
        //supplier2.deliverToWarehouse(warehouse);

        // Выводим текущие запасы на складе
        System.out.println("Текущие запасы на складе:");
        warehouse.printStock();

        // Создаем работников
        Courier courier1 = new Courier("Иван", "Иванов");
        Courier courier2 = new Courier("Петр", "Петров");
        Picker picker1 = new Picker("Алексей", "Алексеев");
        Picker picker2 = new Picker("Сергей", "Сергеев");

        // Работники приходят на смену
        courier1.startShift(warehouse, 4); // 4 часа
        courier2.startShift(warehouse, 6); // 6 часов
        picker1.startShift(warehouse, 8);  // 8 часов
        picker2.startShift(warehouse, 4);  // 4 часа

        // Создаем пользователей
        User user1 = new User("Мария", "Сидорова", new Coordinates(60, 70));
        User user2 = new User("Дмитрий", "Козлов", new Coordinates(30, 40));

        // Пользователи создают заказы
        Order order1 = user1.createOrder();
        order1.addItem("Хлеб", 2);
        order1.addItem("Молоко", 1);
        order1.addItem("Наушники", 1);

        Order order2 = user2.createOrder();
        order2.addItem("Яблоки", 3);
        order2.addItem("Зарядка", 1);

        // Отправляем заказы на склад
        warehouse.processOrder(order1);
        warehouse.processOrder(order2);

        // Ждем некоторое время для обработки заказов
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем статус заказов
        System.out.println("\nСтатус заказа 1: " + order1.getStatus());
        System.out.println("Статус заказа 2: " + order2.getStatus());

        // Завершаем смены работников
        courier1.endShift();
        courier2.endShift();
        picker1.endShift();
        picker2.endShift();

        // Выводим заработок работников
        System.out.println("\nЗаработок работников:");
        System.out.println(courier1.getFullName() + ": " + courier1.getSalary() + " руб.");
        System.out.println(courier2.getFullName() + ": " + courier2.getSalary() + " руб.");
        System.out.println(picker1.getFullName() + ": " + picker1.getSalary() + " руб.");
        System.out.println(picker2.getFullName() + ": " + picker2.getSalary() + " руб.");

        // Тесты
        runTests();
    }

    private static void runTests() {
        System.out.println("\n=== Запуск тестов ===");

        // Тест 1: Создание склада и проверка координат
        Warehouse warehouse = new Warehouse(new Coordinates(10, 20));
        assert warehouse.getCoordinates().getX() == 10 : "Ошибка в координатах склада (X)";
        assert warehouse.getCoordinates().getY() == 20 : "Ошибка в координатах склада (Y)";
        System.out.println("Тест 1 пройден: Создание склада");

        // Тест 2: Добавление товара на склад
        Product product = new Product("Тестовый товар", 100.0);
        warehouse.addToStock(product, 10);
        assert warehouse.getStockQuantity("Тестовый товар") == 10 : "Ошибка в добавлении товара на склад";
        System.out.println("Тест 2 пройден: Добавление товара на склад");

        // Тест 3: Создание пользователя
        User user = new User("Тест", "Тестов", new Coordinates(30, 40));
        assert user.getFullName().equals("Тест Тестов") : "Ошибка в создании пользователя";
        assert user.getCoordinates().getX() == 30 : "Ошибка в координатах пользователя (X)";
        assert user.getCoordinates().getY() == 40 : "Ошибка в координатах пользователя (Y)";
        System.out.println("Тест 3 пройден: Создание пользователя");

        // Тест 4: Создание заказа
        Order order = user.createOrder();
        order.addItem("Тестовый товар", 5);
        assert order.getItems().containsKey("Тестовый товар") : "Ошибка в добавлении товара в заказ";
        assert order.getItems().get("Тестовый товар") == 5 : "Ошибка в количестве товара в заказе";
        System.out.println("Тест 4 пройден: Создание заказа");

        // Тест 5: Расчет расстояния
        Coordinates coord1 = new Coordinates(1, 1);
        Coordinates coord2 = new Coordinates(3, 4);
        double distance = coord1.distanceTo(coord2);
        assert Math.abs(distance - 5.0) < 0.001 : "Ошибка в расчете расстояния";
        System.out.println("Тест 5 пройден: Расчет расстояния");

        // Тест 6: Расчет времени доставки
        double deliveryTime = warehouse.calculateDeliveryTime(user.getCoordinates());
        double expectedTime = 1 + (Math.sqrt(Math.pow(10-30, 2) + Math.pow(20-40, 2)) * 30) / 60 + 1;
        assert Math.abs(deliveryTime - expectedTime) < 0.001 : "Ошибка в расчете времени доставки";
        System.out.println("Тест 6 пройден: Расчет времени доставки");

        // Тест 7: Создание работника и расчет зарплаты
        Courier courier = new Courier("Курьер", "Тестовый");
        courier.startShift(warehouse, 4);
        courier.endShift();
        assert Math.abs(courier.getSalary() - 1200.0) < 0.001 : "Ошибка в расчете зарплаты";
        System.out.println("Тест 7 пройден: Создание работника и расчет зарплаты");

        System.out.println("Все тесты пройдены успешно!");
    }
}