package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;

public interface VendingMachineState {
    void insertMoney(VendingMachine machine, Denomination denomination);
    void selectProduct(VendingMachine machine, String slotId, int quantity);
    void dispense(VendingMachine machine);
    void cancel(VendingMachine machine);
}
