package ParkingLot.strategy;

import java.math.BigDecimal;

public class UpiPayment implements PaymentStrategy{
    @Override
    public void processPayment(BigDecimal amount){
        System.out.println("Payment processed");
    }
}
