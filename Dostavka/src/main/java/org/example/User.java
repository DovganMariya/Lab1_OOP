package org.example;

class User {
    private final String firstName;
    private final String lastName;
    private final Coordinates coordinates;

    public User(String firstName, String lastName, Coordinates coordinates) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.coordinates = coordinates;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Order createOrder() {
        return new Order(this);
    }
}