package com.billcraft.service;

import com.billcraft.model.MenuItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Holds the restaurant's menu and provides lookup/search operations.
 * Pre-populated with a default menu, but new items can be added at
 * runtime through the admin menu.
 */
public class Menu {

    private final List<MenuItem> items;
    private int nextId;

    public Menu() {
        this.items = new ArrayList<>();
        this.nextId = 1;
        loadDefaultMenu();
    }

    private void loadDefaultMenu() {
        addItem("Paneer Butter Masala", "Main Course", 220.00);
        addItem("Chicken Biryani", "Main Course", 260.00);
        addItem("Veg Fried Rice", "Main Course", 150.00);
        addItem("Butter Naan", "Bread", 40.00);
        addItem("Tandoori Roti", "Bread", 25.00);
        addItem("Masala Dosa", "South Indian", 90.00);
        addItem("Idli Sambar", "South Indian", 70.00);
        addItem("Chicken 65", "Starter", 190.00);
        addItem("Veg Manchurian", "Starter", 140.00);
        addItem("Gulab Jamun", "Dessert", 60.00);
        addItem("Cold Coffee", "Beverage", 80.00);
        addItem("Masala Chai", "Beverage", 30.00);
    }

    /**
     * Adds a new item to the menu with an auto-generated ID.
     */
    public MenuItem addItem(String name, String category, double price) {
        MenuItem item = new MenuItem(nextId++, name, category, price);
        items.add(item);
        return item;
    }

    public List<MenuItem> getAllItems() {
        return items.stream()
                .sorted(Comparator.comparing(MenuItem::getCategory).thenComparing(MenuItem::getId))
                .toList();
    }

    public Optional<MenuItem> findById(int id) {
        return items.stream().filter(item -> item.getId() == id).findFirst();
    }

    public boolean removeItem(int id) {
        return items.removeIf(item -> item.getId() == id);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
