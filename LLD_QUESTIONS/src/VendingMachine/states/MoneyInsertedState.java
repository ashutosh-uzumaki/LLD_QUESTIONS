package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;
import VendingMachine.models.Product;
import VendingMachine.models.Slot;

public class MoneyInsertedState implements VendingMachineState{
    @Override
    public void insertMoney(VendingMachine machine, Denomination denomination) {
        machine.addMoney(denomination);
    }

    @Override
    public void cancel(VendingMachine machine) {
        machine.reset();
    }

    @Override
    public void dispense(VendingMachine machine) {

    }

    @Override
    public void selectProduct(VendingMachine machine, String slotId, int quantity) {
        Slot slot = machine.findSlot(slotId);
        if(slot == null){
            System.out.println("Please select correct slot id");
            return;
        }
        if(slot.getCount() < quantity){
            System.out.println("The quantity you have selected is greater than available stock");
        }
        Product product = slot.getProduct();
        machine.setCurrentState(new ProductSelectedState());
        machine.selectProduct(slotId, quantity);
    }
}
