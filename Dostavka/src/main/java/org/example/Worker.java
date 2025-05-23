package org.example;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class Worker {
    private final String firstName;
    private final String lastName;
    protected Warehouse warehouse;
    protected LocalDateTime shiftStartTime;
    protected LocalDateTime shiftEndTime;
    protected boolean onShift;
    protected double salary;
    protected static final double HOURLY_RATE = 300.0; // 300 руб/час

    public Worker(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.onShift = false;
        this.salary = 0.0;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isOnShift() {
        return onShift;
    }

    public double getSalary() {
        return salary;
    }

    public void startShift(Warehouse warehouse, int hours) {
        if (onShift) {
            throw new IllegalStateException("Работник уже на смене");
        }
        this.warehouse = warehouse;
        this.shiftStartTime = LocalDateTime.now();
        this.shiftEndTime = shiftStartTime.plusHours(hours);
        this.onShift = true;
        warehouse.addWorker(this);
        System.out.println(getFullName() + " начал смену на " + hours + " часов");
    }

    public void endShift() {
        if (!onShift) {
            throw new IllegalStateException("Работник не на смене");
        }

        LocalDateTime actualEndTime = LocalDateTime.now();
        if (actualEndTime.isAfter(shiftEndTime)) {
            actualEndTime = shiftEndTime;
        }

        Duration duration = Duration.between(shiftStartTime, actualEndTime);
        double hoursWorked = duration.toSeconds() / 3600.0;
        this.salary += hoursWorked * HOURLY_RATE;

        this.onShift = false;
        warehouse.removeWorker(this);
        System.out.println(getFullName() + " закончил смену, заработав " +
                String.format("%.2f", hoursWorked * HOURLY_RATE) + " руб.");
    }

    abstract void processTask();
}