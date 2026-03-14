package ParkingLot.strategy;

import ParkingLot.models.Ticket;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateParkingFee(Ticket ticket);
}
