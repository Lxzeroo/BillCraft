package com.billcraft;

import com.billcraft.model.Bill;
import com.billcraft.model.MenuItem;
import com.billcraft.model.Order;
import com.billcraft.service.BillingService;
import com.billcraft.service.Menu;
import com.billcraft.util.BillPrinter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * BillCraft - A console-based restaurant billing system.
 *
 * Lets staff browse the menu, build an order for a customer/table,
 * and generate a finalized, tax-calculated bill that is printed to
 * the console and saved to a text file.
 */
public class BillCraftApp {

    private final Scanner scanner = new Scanner(System.in);
    private final Menu menu = new Menu();
    private final BillingService billingService = new BillingService();
    private final BillPrinter billPrinter = new BillPrinter();

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        new BillCraftApp().run();
    }

    public void run() {
        printWelcomeBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> startNewOrder();
                case 2 -> viewMenu();
                case 3 -> addMenuItem();
                case 4 -> removeMenuItem();
                case 0 -> {
                    System.out.println("\nThank you for using BillCraft. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ---------------------------------------------------------------
    // Main menu actions
    // ---------------------------------------------------------------

    private void startNewOrder() {
        if (menu.isEmpty()) {
            System.out.println("\nThe menu is empty. Please add items first (Option 3).");
            return;
        }

        System.out.print("\nEnter customer name: ");
        String customerName = scanner.nextLine().trim();
        if (customerName.isEmpty()) {
            customerName = "Guest";
        }

        int tableNumber = readInt("Enter table number: ");
        Order order = new Order(customerName, tableNumber);

        boolean ordering = true;
        while (ordering) {
            printOrderMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> viewMenu();
                case 2 -> addItemToOrder(order);
                case 3 -> removeItemFromOrder(order);
                case 4 -> viewCurrentOrder(order);
                case 5 -> {
                    if (finalizeAndPrintBill(order)) {
                        ordering = false;
                    }
                }
                case 0 -> {
                    System.out.println("Order cancelled.");
                    ordering = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addItemToOrder(Order order) {
        viewMenu();
        int id = readInt("\nEnter item ID to add: ");
        var itemOpt = menu.findById(id);

        if (itemOpt.isEmpty()) {
            System.out.println("No item found with ID " + id + ".");
            return;
        }

        int quantity = readInt("Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("Quantity must be at least 1.");
            return;
        }

        order.addItem(itemOpt.get(), quantity);
        System.out.printf("Added %d x %s to the order.%n", quantity, itemOpt.get().getName());
    }

    private void removeItemFromOrder(Order order) {
        if (order.isEmpty()) {
            System.out.println("\nOrder is currently empty.");
            return;
        }

        viewCurrentOrder(order);
        int id = readInt("\nEnter item ID to remove: ");
        boolean removed = order.removeItem(id);
        System.out.println(removed ? "Item removed from order." : "Item not found in order.");
    }

    private void viewCurrentOrder(Order order) {
        System.out.println();
        if (order.isEmpty()) {
            System.out.println("Order is currently empty.");
            return;
        }

        System.out.println("----- Current Order -----");
        order.getItems().forEach(item -> System.out.println(item));
        System.out.printf("Running subtotal: ₹%.2f%n", order.getSubtotal());
        System.out.println("--------------------------");
    }

    private boolean finalizeAndPrintBill(Order order) {
        if (order.isEmpty()) {
            System.out.println("\nCannot generate a bill for an empty order. Add items first.");
            return false;
        }

        Bill bill = billingService.generateBill(order);
        String receipt = billPrinter.format(bill);

        System.out.println();
        System.out.println(receipt);

        try {
            Path savedPath = billPrinter.saveToFile(bill);
            System.out.println("\nBill saved to: " + savedPath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("\nWarning: could not save bill to file (" + e.getMessage() + ").");
        }

        return true;
    }

    private void viewMenu() {
        List<MenuItem> items = menu.getAllItems();
        System.out.println();
        if (items.isEmpty()) {
            System.out.println("Menu is currently empty.");
            return;
        }

        System.out.println("============ MENU ============");
        String currentCategory = "";
        for (MenuItem item : items) {
            if (!item.getCategory().equals(currentCategory)) {
                currentCategory = item.getCategory();
                System.out.println("-- " + currentCategory + " --");
            }
            System.out.println(item);
        }
        System.out.println("===============================");
    }

    private void addMenuItem() {
        System.out.print("\nEnter item name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }

        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) {
            category = "General";
        }

        double price = readDouble("Enter price: ");
        if (price < 0) {
            System.out.println("Price cannot be negative.");
            return;
        }

        MenuItem item = menu.addItem(name, category, price);
        System.out.println("Added: " + item);
    }

    private void removeMenuItem() {
        viewMenu();
        int id = readInt("\nEnter item ID to remove: ");
        boolean removed = menu.removeItem(id);
        System.out.println(removed ? "Item removed from menu." : "No item found with that ID.");
    }

    // ---------------------------------------------------------------
    // UI helpers
    // ---------------------------------------------------------------

    private void printWelcomeBanner() {
        System.out.println("=================================================");
        System.out.println("        BILLCRAFT - RESTAURANT BILLING SYSTEM");
        System.out.println("=================================================");
    }

    private void printMainMenu() {
        System.out.println("\n----------- MAIN MENU -----------");
        System.out.println("1. Start New Order");
        System.out.println("2. View Menu");
        System.out.println("3. Add Menu Item (Admin)");
        System.out.println("4. Remove Menu Item (Admin)");
        System.out.println("0. Exit");
        System.out.println("----------------------------------");
    }

    private void printOrderMenu() {
        System.out.println("\n------- ORDER MENU -------");
        System.out.println("1. View Menu");
        System.out.println("2. Add Item to Order");
        System.out.println("3. Remove Item from Order");
        System.out.println("4. View Current Order");
        System.out.println("5. Finalize & Generate Bill");
        System.out.println("0. Cancel Order");
        System.out.println("---------------------------");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
