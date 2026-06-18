package com.billcraft.service;

import com.billcraft.model.Bill;
import com.billcraft.model.Order;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Applies tax (CGST + SGST) and optional discount rules to an Order
 * and produces a finalized, immutable Bill.
 */
public class BillingService {

    private static final double CGST_RATE = 0.025; // 2.5%
    private static final double SGST_RATE = 0.025; // 2.5%

    /** Orders above this subtotal automatically receive a discount. */
    private static final double DISCOUNT_THRESHOLD = 1000.0;
    private static final double DISCOUNT_PERCENT = 10.0; // 10%

    private final AtomicInteger billSequence = new AtomicInteger(1);

    /**
     * Generates a finalized Bill from the given order, applying tax
     * and an automatic loyalty discount if the subtotal qualifies.
     */
    public Bill generateBill(Order order) {
        double subtotal = order.getSubtotal();

        double discountPercent = subtotal >= DISCOUNT_THRESHOLD ? DISCOUNT_PERCENT : 0.0;
        double discountAmount = subtotal * (discountPercent / 100.0);
        double taxableAmount = subtotal - discountAmount;

        double cgstAmount = taxableAmount * CGST_RATE;
        double sgstAmount = taxableAmount * SGST_RATE;

        double grandTotal = taxableAmount + cgstAmount + sgstAmount;

        String billId = generateBillId();

        return new Bill(billId, order, subtotal,
                CGST_RATE * 100, SGST_RATE * 100,
                cgstAmount, sgstAmount,
                discountPercent, discountAmount,
                grandTotal);
    }

    private String generateBillId() {
        return String.format("BC-%04d", billSequence.getAndIncrement());
    }

    public double getDiscountThreshold() {
        return DISCOUNT_THRESHOLD;
    }
}
