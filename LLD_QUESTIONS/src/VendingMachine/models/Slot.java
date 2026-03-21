package VendingMachine.models;

public class Slot {
    private final String slotId;
    private final Product product;
    private int count;

    public Slot(String slotId, Product product, int count){
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
            throw new IllegalStateException("Slot is empty");
        }
        this.count -= qty;
    }

    public boolean isAvailable() {
        return count > 0;
    }
}
