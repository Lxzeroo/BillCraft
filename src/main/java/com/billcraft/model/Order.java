package com.billcraft.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a customer's order: a collection of OrderItems plus
 * customer/table metadata used when generating the final bill.
 */
public class Order {

    private final String customerName;
    private final int tableNumber;
    private final List<OrderItem> items;

    public Order(String customerName, int tableNumber) {
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.items = new ArrayList<>();
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Adds a menu item to the order. If the item already exists in
     * the order, its quantity is incremented instead of creating a
     * duplicate line.
     */
    public void addItem(MenuItem menuItem, int quantity) {
        Optional<OrderItem> existing = findOrderItem(menuItem.getId());
        if (existing.isPresent()) {
            existing.get().incrementQuantity(quantity);
        } else {
            items.add(new OrderItem(menuItem, quantity));
        }
    }

    /**
     * Removes a menu item entirely from the order. Returns true if
     * an item was found and removed.
     */
    public boolean removeItem(int menuItemId) {
        return items.removeIf(orderItem -> orderItem.getMenuItem().getId() == menuItemId);
    }

    public Optional<OrderItem> findOrderItem(int menuItemId) {
        return items.stream()
                .filter(orderItem -> orderItem.getMenuItem().getId() == menuItemId)
                .findFirst();
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }
}
