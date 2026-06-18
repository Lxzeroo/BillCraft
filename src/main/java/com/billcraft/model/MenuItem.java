package com.billcraft.model;

/**
 * Represents a single item on the restaurant's menu.
 */
public class MenuItem {

    private final int id;
    private final String name;
    private final String category;
    private final double price;

    public MenuItem(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("[%2d] %-22s %-10s ₹%8.2f", id, name, category, price);
    }
}
