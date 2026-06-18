package com.billcraft.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a finalized bill generated from an Order, including
 * tax and discount calculations. Immutable once created.
 */
public class Bill {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final String billId;
    private final Order order;
    private final double subtotal;
    private final double cgstRate;
    private final double sgstRate;
    private final double cgstAmount;
    private final double sgstAmount;
    private final double discountPercent;
    private final double discountAmount;
    private final double grandTotal;
    private final LocalDateTime generatedAt;

    public Bill(String billId, Order order, double subtotal,
                double cgstRate, double sgstRate,
                double cgstAmount, double sgstAmount,
                double discountPercent, double discountAmount,
                double grandTotal) {
        this.billId = billId;
        this.order = order;
        this.subtotal = subtotal;
        this.cgstRate = cgstRate;
        this.sgstRate = sgstRate;
        this.cgstAmount = cgstAmount;
        this.sgstAmount = sgstAmount;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.grandTotal = grandTotal;
        this.generatedAt = LocalDateTime.now();
    }

    public String getBillId() {
        return billId;
    }

    public Order getOrder() {
        return order;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getCgstRate() {
        return cgstRate;
    }

    public double getSgstRate() {
        return sgstRate;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public String getFormattedTimestamp() {
        return generatedAt.format(TIMESTAMP_FORMAT);
    }
}
