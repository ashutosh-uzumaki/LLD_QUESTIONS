package ParkingLot.strategy;

import java.math.BigDecimal;

public interface PaymentStrategy {
    void processPayment(BigDecimal amount);
}
