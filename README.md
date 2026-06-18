# BillCraft

BillCraft is a console-based restaurant billing system written entirely in Java. It lets restaurant staff browse a food menu, build an order for a customer and table, and generate a finalized, tax-calculated bill that is printed to the console and saved as a text file.

## Features

- Menu management: view items by category, add new items, remove items (admin mode)
- Order building: add/remove items by ID, adjust quantity, view a running subtotal before finalizing
- Automatic billing: applies CGST and SGST (2.5% each) on every bill
- Automatic discount: orders with a subtotal of ₹1000 or more get a 10% discount before tax
- Bill generation: produces a formatted, itemized receipt with a unique bill number and timestamp
- Bill persistence: every finalized bill is saved as a `.txt` file under `bills/`

## Project structure

```
BillCraft/
├── src/main/java/com/billcraft/
│   ├── BillCraftApp.java        Entry point and console UI flow
│   ├── model/
│   │   ├── MenuItem.java        A single menu item (id, name, category, price)
│   │   ├── OrderItem.java       A menu item plus quantity within an order
│   │   ├── Order.java           A customer's full order (list of OrderItems)
│   │   └── Bill.java            A finalized, tax-calculated bill
│   ├── service/
│   │   ├── Menu.java            Holds and manages the list of menu items
│   │   └── BillingService.java  Tax and discount calculation logic
│   └── util/
│       └── BillPrinter.java     Formats a Bill into a printable receipt and saves it to file
├── bills/                       Generated bill receipts are saved here
├── build.sh / build.bat         Build scripts for Linux/macOS and Windows
├── run.sh / run.bat             Run scripts for Linux/macOS and Windows
└── .gitignore
```

The project uses plain `javac`/`java` with no external dependencies or build tools (no Maven/Gradle required), so it runs on any machine with a JDK installed.

## Requirements

- JDK 17 or later

## How to build and run

On Linux/macOS:

```bash
./build.sh
./run.sh
```

On Windows:

```cmd
build.bat
run.bat
```

Or compile and run manually:

```bash
mkdir -p bin
find src/main/java -name "*.java" > sources.txt
javac -d bin -encoding UTF-8 @sources.txt
java -Dfile.encoding=UTF-8 -cp bin com.billcraft.BillCraftApp
```

## Usage walkthrough

1. From the main menu, choose **1. Start New Order** and enter the customer's name and table number.
2. Use **2. Add Item to Order** to browse the menu and add items by ID with a quantity. Adding the same item again increases its quantity rather than duplicating the line.
3. Use **4. View Current Order** at any time to see a running subtotal.
4. Choose **5. Finalize & Generate Bill** to compute tax and discount, print the receipt, and save it to `bills/<bill-id>.txt`.
5. From the main menu, admins can use **3. Add Menu Item** and **4. Remove Menu Item** to manage the menu without touching code.

## Sample bill output

```
================================================
              BILLCRAFT RESTAURANT
            Your Bill, Crafted Right
================================================
Bill No : BC-0001
Date    : 18-06-2026 06:16:53
Customer: Rahul Sharma
Table No: 5
------------------------------------------------
Item                   Qty  Price         Amount
------------------------------------------------
Chicken Biryani        x3   ₹  260.00 ₹   780.00
Chicken 65             x2   ₹  190.00 ₹   380.00
------------------------------------------------
Subtotal                       ₹       1160.00
Discount (10.0%)               -₹        116.00
CGST (2.5%)                    ₹         26.10
SGST (2.5%)                    ₹         26.10
------------------------------------------------
GRAND TOTAL                    ₹        1096.20
================================================
         Thank you for dining with us!
                 Visit again :)
================================================
```

## License

This project is free to use and modify for personal or educational purposes.
