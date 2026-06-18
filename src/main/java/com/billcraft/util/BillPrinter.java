package com.billcraft.util;

import com.billcraft.model.Bill;
import com.billcraft.model.OrderItem;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Formats a Bill into a human-readable receipt and optionally
 * persists it to disk under the bills/ directory.
 */
public class BillPrinter {

    private static final int WIDTH = 48;
    private static final String LINE = "=".repeat(WIDTH);
    private static final String THIN_LINE = "-".repeat(WIDTH);

    /**
     * Builds the full receipt text for the given bill.
     */
    public String format(Bill bill) {
        StringBuilder sb = new StringBuilder();

        sb.append(LINE).append(System.lineSeparator());
        sb.append(center("BILLCRAFT RESTAURANT")).append(System.lineSeparator());
        sb.append(center("Your Bill, Crafted Right")).append(System.lineSeparator());
        sb.append(LINE).append(System.lineSeparator());

        sb.append(String.format("Bill No : %s%n", bill.getBillId()));
        sb.append(String.format("Date    : %s%n", bill.getFormattedTimestamp()));
        sb.append(String.format("Customer: %s%n", bill.getOrder().getCustomerName()));
        sb.append(String.format("Table No: %d%n", bill.getOrder().getTableNumber()));
        sb.append(THIN_LINE).append(System.lineSeparator());

        sb.append(String.format("%-22s %-4s %-9s %10s%n", "Item", "Qty", "Price", "Amount"));
        sb.append(THIN_LINE).append(System.lineSeparator());

        for (OrderItem item : bill.getOrder().getItems()) {
            sb.append(String.format("%-22s x%-3d ₹%8.2f ₹%9.2f%n",
                    truncate(item.getMenuItem().getName(), 22),
                    item.getQuantity(),
                    item.getMenuItem().getPrice(),
                    item.getSubtotal()));
        }

        sb.append(THIN_LINE).append(System.lineSeparator());
        sb.append(rightAlignRow("Subtotal", bill.getSubtotal()));

        if (bill.getDiscountPercent() > 0) {
            sb.append(rightAlignRow(
                    String.format("Discount (%.1f%%)", bill.getDiscountPercent()),
                    -bill.getDiscountAmount()));
        }

        sb.append(rightAlignRow(
                String.format("CGST (%.1f%%)", bill.getCgstRate()), bill.getCgstAmount()));
        sb.append(rightAlignRow(
                String.format("SGST (%.1f%%)", bill.getSgstRate()), bill.getSgstAmount()));

        sb.append(THIN_LINE).append(System.lineSeparator());
        sb.append(String.format("%-30s ₹%15.2f%n", "GRAND TOTAL", bill.getGrandTotal()));
        sb.append(LINE).append(System.lineSeparator());
        sb.append(center("Thank you for dining with us!")).append(System.lineSeparator());
        sb.append(center("Visit again :)")).append(System.lineSeparator());
        sb.append(LINE);

        return sb.toString();
    }

    private String rightAlignRow(String label, double amount) {
        String sign = amount < 0 ? "-" : "";
        return String.format("%-30s %s₹%14.2f%n", label, sign, Math.abs(amount));
    }

    private String center(String text) {
        int padding = Math.max(0, (WIDTH - text.length()) / 2);
        return " ".repeat(padding) + text;
    }

    private String truncate(String text, int maxLength) {
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 1) + "…";
    }

    /**
     * Saves the formatted bill to a text file under bills/ directory,
     * named after the bill ID. Returns the path written to.
     */
    public Path saveToFile(Bill bill) throws IOException {
        Path billsDir = Paths.get("bills");
        if (!Files.exists(billsDir)) {
            Files.createDirectories(billsDir);
        }

        Path filePath = billsDir.resolve(bill.getBillId() + ".txt");
        try (OutputStreamWriter writer = new OutputStreamWriter(
                Files.newOutputStream(filePath), StandardCharsets.UTF_8)) {
            writer.write(format(bill));
        }
        return filePath;
    }
}
