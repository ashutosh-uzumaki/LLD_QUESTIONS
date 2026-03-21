// ============================================================
// FILE 1: Denomination.java (Enum)
// ============================================================
package VendingMachine.enums;

public enum Denomination {
    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


// ============================================================
// FILE 2: Product.java (Model)
// ============================================================
package VendingMachine.models;

import java.math.BigDecimal;

public class Product {
    private final String name;
    private final BigDecimal price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}


// ============================================================
// FILE 3: Slot.java (Model)
// ============================================================
package VendingMachine.models;

public class Slot {
    private final String slotId;
    private final Product product;
    private int count;

    public Slot(String slotId, Product product, int count) {
        this.slotId = slotId;
        this.product = product;
        this.count = count;
    }

    public String getSlotId() {
        return slotId;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public void reduceQuantity(int qty) {
        if (qty > count) {
            throw new IllegalStateException("Not enough stock. Available: " + count + ", Requested: " + qty);
        }
        this.count -= qty;
    }

    public boolean isAvailable() {
        return count > 0;
    }
}


// ============================================================
// FILE 4: VendingMachineState.java (State Interface)
// ============================================================
package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;

public interface VendingMachineState {
    void insertMoney(VendingMachine machine, Denomination denomination);
    void selectProduct(VendingMachine machine, String slotId, int quantity);
    void dispense(VendingMachine machine);
    void cancel(VendingMachine machine);
}


// ============================================================
// FILE 5: IdleState.java
// ============================================================
package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;

public class IdleState implements VendingMachineState {

    @Override
    public void insertMoney(VendingMachine machine, Denomination denomination) {
        // VALID: Accept money, transition to MoneyInsertedState
        // WHY: Idle means waiting for customer. Inserting money starts the transaction.
        machine.addMoney(denomination);
        machine.setState(new MoneyInsertedState());
        System.out.println("Inserted: ₹" + denomination.getValue() +
                ". Total: ₹" + machine.getInsertedAmount());
    }

    @Override
    public void selectProduct(VendingMachine machine, String slotId, int quantity) {
        // REJECTED: Can't select without inserting money first
        // WHY: This is cash-first flow. Money must come before selection.
        System.out.println("Please insert money first");
    }

    @Override
    public void dispense(VendingMachine machine) {
        // REJECTED: Nothing to dispense
        // WHY: No money inserted, no product selected.
        System.out.println("Please insert money first");
    }

    @Override
    public void cancel(VendingMachine machine) {
        // REJECTED: Nothing to cancel
        // WHY: No transaction in progress.
        System.out.println("No transaction to cancel");
    }
}


// ============================================================
// FILE 6: MoneyInsertedState.java
// ============================================================
package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;
import VendingMachine.models.Product;
import VendingMachine.models.Slot;

import java.math.BigDecimal;

public class MoneyInsertedState implements VendingMachineState {

    @Override
    public void insertMoney(VendingMachine machine, Denomination denomination) {
        // VALID: Accept more money, stay in same state
        // WHY: User might need to insert multiple coins to reach the product price.
        // No state transition because user may want to insert more.
        machine.addMoney(denomination);
        System.out.println("Inserted: ₹" + denomination.getValue() +
                ". Total: ₹" + machine.getInsertedAmount());
    }

    @Override
    public void selectProduct(VendingMachine machine, String slotId, int quantity) {
        // VALID: Select a product — but must pass 3 validations first
        // WHY: This is the core business logic. We validate slot, stock, and money.

        // Step 1: Find the slot — does this slot ID exist?
        Slot slot = machine.findSlot(slotId);
        if (slot == null) {
            System.out.println("Invalid slot: " + slotId);
            return; // Stay in MoneyInsertedState — user can try another slot
        }

        // Step 2: Check stock — does the slot have enough quantity?
        if (slot.getCount() < quantity) {
            System.out.println("Not enough stock. Available: " + slot.getCount());
            return; // Stay in MoneyInsertedState — user can select different product
        }

        // Step 3: Check money — has user inserted enough?
        Product product = slot.getProduct();
        BigDecimal totalCost = product.getPrice().multiply(new BigDecimal(quantity));
        if (machine.getInsertedAmount().compareTo(totalCost) < 0) {
            System.out.println("Insufficient money. Need: ₹" + totalCost +
                    ", Inserted: ₹" + machine.getInsertedAmount() +
                    ". Please insert ₹" + totalCost.subtract(machine.getInsertedAmount()) + " more");
            return; // Stay in MoneyInsertedState — user can insert more money
        }

        // Step 4: All checks passed — save selection and transition
        // WHY we store on machine: ProductSelectedState and DispensingState need this data later.
        // Machine is the shared context all states read from.
        machine.setSelectedSlot(slot);
        machine.setSelectedQuantity(quantity);
        machine.setState(new ProductSelectedState());
        System.out.println("Selected: " + product.getName() + " x" + quantity +
                ". Total cost: ₹" + totalCost);
    }

    @Override
    public void dispense(VendingMachine machine) {
        // REJECTED: Must select a product before dispensing
        System.out.println("Please select a product first");
    }

    @Override
    public void cancel(VendingMachine machine) {
        // VALID: Refund all money and go back to Idle
        // WHY: User changed their mind. Return their money, clear everything.
        System.out.println("Transaction cancelled. Refunding: ₹" + machine.getInsertedAmount());
        machine.reset(); // reset() clears amount, selection, sets state to IdleState
    }
}


// ============================================================
// FILE 7: ProductSelectedState.java
// ============================================================
package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;
import VendingMachine.models.Product;
import VendingMachine.models.Slot;

import java.math.BigDecimal;

public class ProductSelectedState implements VendingMachineState {

    @Override
    public void insertMoney(VendingMachine machine, Denomination denomination) {
        // REJECTED: Product already selected, no more money needed
        System.out.println("Product already selected. Please collect your item");
    }

    @Override
    public void selectProduct(VendingMachine machine, String slotId, int quantity) {
        // REJECTED: Already selected a product
        System.out.println("Product already selected. Please collect your item");
    }

    @Override
    public void dispense(VendingMachine machine) {
        // VALID: Dispense the product and return change
        // WHY: This is the final step. Product is selected, money is sufficient. Give the product.

        Slot slot = machine.getSelectedSlot();
        Product product = slot.getProduct();
        int quantity = machine.getSelectedQuantity();

        // Step 1: Calculate total cost
        BigDecimal totalCost = product.getPrice().multiply(new BigDecimal(quantity));

        // Step 2: Calculate change
        BigDecimal change = machine.getInsertedAmount().subtract(totalCost);

        // Step 3: Reduce inventory
        // WHY: Product has been given out. Update the count so next customer sees accurate stock.
        slot.reduceQuantity(quantity);

        // Step 4: Dispense product
        System.out.println("Dispensing: " + product.getName() + " x" + quantity);

        // Step 5: Return change if any
        if (change.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Returning change: ₹" + change);
        }

        // Step 6: Reset machine for next customer
        // WHY: Transaction complete. Clear everything. Go back to Idle for next customer.
        System.out.println("Thank you! Please collect your product.");
        machine.reset();
    }

    @Override
    public void cancel(VendingMachine machine) {
        // VALID: User changed mind after selection. Refund and reset.
        // WHY: Product not dispensed yet, so we can still cancel.
        System.out.println("Transaction cancelled. Refunding: ₹" + machine.getInsertedAmount());
        machine.reset();
    }
}


// ============================================================
// FILE 8: VendingMachine.java (Context — the main class)
// ============================================================
package VendingMachine;

import VendingMachine.enums.Denomination;
import VendingMachine.models.Slot;
import VendingMachine.states.IdleState;
import VendingMachine.states.VendingMachineState;

import java.math.BigDecimal;
import java.util.List;

public class VendingMachine {
    private final List<Slot> slots;
    private VendingMachineState currentState;
    private BigDecimal insertedAmount;
    private Slot selectedSlot;
    private int selectedQuantity;

    public VendingMachine(List<Slot> slots) {
        this.slots = slots;
        this.currentState = new IdleState();
        this.insertedAmount = BigDecimal.ZERO;
        this.selectedSlot = null;
        this.selectedQuantity = 0;
    }

    // ==========================================
    // DELEGATING METHODS — User calls these
    // Each one delegates to currentState
    // VendingMachine doesn't know what state it's in
    // ==========================================

    public void insertMoney(Denomination denomination) {
        currentState.insertMoney(this, denomination);
    }

    public void selectProduct(String slotId, int quantity) {
        currentState.selectProduct(this, slotId, quantity);
    }

    public void dispense() {
        currentState.dispense(this);
    }

    public void cancel() {
        currentState.cancel(this);
    }

    // ==========================================
    // HELPER METHODS — States call these
    // ==========================================

    // Adds denomination value to running total
    public void addMoney(Denomination denomination) {
        insertedAmount = insertedAmount.add(new BigDecimal(denomination.getValue()));
    }

    // Finds a slot by ID — used by MoneyInsertedState.selectProduct()
    public Slot findSlot(String slotId) {
        for (Slot slot : slots) {
            if (slot.getSlotId().equals(slotId)) {
                return slot;
            }
        }
        return null;
    }

    // Resets machine to initial state — called after dispense or cancel
    public void reset() {
        this.insertedAmount = BigDecimal.ZERO;
        this.selectedSlot = null;
        this.selectedQuantity = 0;
        this.currentState = new IdleState();
    }

    // ==========================================
    // GETTERS & SETTERS — States read/write these
    // ==========================================

    public BigDecimal getInsertedAmount() {
        return insertedAmount;
    }

    public Slot getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(Slot slot) {
        this.selectedSlot = slot;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int qty) {
        this.selectedQuantity = qty;
    }

    public VendingMachineState getCurrentState() {
        return currentState;
    }

    public void setState(VendingMachineState state) {
        this.currentState = state;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}


// ============================================================
// FILE 9: VendingMachineDemo.java (Test)
// ============================================================
package VendingMachine;

import VendingMachine.enums.Denomination;
import VendingMachine.models.Product;
import VendingMachine.models.Slot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineDemo {
    public static void main(String[] args) {
        // Setup products
        Product coke = new Product("Coke", new BigDecimal("30"));
        Product pepsi = new Product("Pepsi", new BigDecimal("25"));
        Product chips = new Product("Chips", new BigDecimal("20"));
        Product water = new Product("Water", new BigDecimal("15"));

        // Setup slots
        List<Slot> slots = new ArrayList<>();
        slots.add(new Slot("A1", coke, 5));
        slots.add(new Slot("A2", pepsi, 3));
        slots.add(new Slot("B1", chips, 10));
        slots.add(new Slot("B2", water, 8));

        // Create machine
        VendingMachine machine = new VendingMachine(slots);

        System.out.println("========== TEST 1: Normal Purchase ==========");
        // Insert ₹50 (₹20 + ₹20 + ₹10)
        machine.insertMoney(Denomination.TWENTY);
        machine.insertMoney(Denomination.TWENTY);
        machine.insertMoney(Denomination.TEN);
        // Select Coke (₹30) — should get ₹20 change
        machine.selectProduct("A1", 1);
        machine.dispense();

        System.out.println("\n========== TEST 2: Insufficient Money ==========");
        machine.insertMoney(Denomination.TEN);
        // Try to buy Coke (₹30) with only ₹10
        machine.selectProduct("A1", 1);
        // Insert more
        machine.insertMoney(Denomination.TWENTY);
        // Try again — now ₹30, exact amount
        machine.selectProduct("A1", 1);
        machine.dispense();

        System.out.println("\n========== TEST 3: Cancel and Refund ==========");
        machine.insertMoney(Denomination.FIFTY);
        machine.insertMoney(Denomination.TWENTY);
        // Cancel — should refund ₹70
        machine.cancel();

        System.out.println("\n========== TEST 4: Invalid Slot ==========");
        machine.insertMoney(Denomination.HUNDRED);
        machine.selectProduct("Z9", 1);  // Invalid slot
        machine.selectProduct("A1", 1);  // Valid slot
        machine.dispense();

        System.out.println("\n========== TEST 5: Dispense without selecting ==========");
        machine.insertMoney(Denomination.TWENTY);
        machine.dispense();  // Should say "select product first"
        machine.cancel();    // Cleanup

        System.out.println("\n========== TEST 6: Select without money ==========");
        machine.selectProduct("A1", 1);  // Should say "insert money first"

        System.out.println("\n========== TEST 7: Multiple Quantity ==========");
        machine.insertMoney(Denomination.HUNDRED);
        machine.selectProduct("B1", 3);  // 3 chips = ₹60, change = ₹40
        machine.dispense();
    }
}