package org.example;

// Класс для представления координат
class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        if (x < 1 || x > 100 || y < 1 || y > 100) {
            throw new IllegalArgumentException("Координаты должны быть в диапазоне от 1 до 100");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distanceTo(Coordinates other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}