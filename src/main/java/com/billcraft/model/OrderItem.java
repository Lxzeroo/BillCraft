package com.billcraft.model;

/**
 * Represents a line item in an order: a MenuItem along with the
 * quantity ordered. Quantity can be increased if the same item
 * is added again.
 */
public class OrderItem {

    private final MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity(int amount) {
        this.quantity += amount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return menuItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-22s x%-3d ₹%8.2f = ₹%9.2f",
                menuItem.getName(), quantity, menuItem.getPrice(), getSubtotal());
    }
}
