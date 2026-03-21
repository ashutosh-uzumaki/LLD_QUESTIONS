package VendingMachine.states;

import VendingMachine.VendingMachine;
import VendingMachine.enums.Denomination;

public class IdleState implements VendingMachineState{

    @Override
    public void insertMoney(VendingMachine machine, Denomination denomination) {
        machine.setCurrentState(new MoneyInsertedState());
        machine.addMoney(denomination);
        System.out.println("Inserted: ₹" + denomination.getValue() +
                ". Total: ₹" + machine.getAmountInserted());
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("Please Insert Money First");
    }

    @Override
    public void dispense(VendingMachine machine) {
        System.out.println("Please Insert Money First");
    }

    @Override
    public void selectProduct(VendingMachine machine, String slotId, int quantity) {
        System.out.println("Please Insert Money First");
    }
}
