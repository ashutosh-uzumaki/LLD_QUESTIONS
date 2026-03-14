package ParkingLot.strategy;

import java.math.BigDecimal;

public class CashPayment implements PaymentStrategy{
    @Override
    public void processPayment(BigDecimal amount){
        System.out.println("Payment has been processed");
    }
}
