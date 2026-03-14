package ParkingLot.strategy;

import ParkingLot.enums.VehicleType;
import ParkingLot.models.Ticket;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy{
    private static final Map<VehicleType, BigDecimal> HOURLY_RATES = Map.of(
            VehicleType.BIKE, new BigDecimal("20"),
            VehicleType.CAR, new BigDecimal("40"),
            VehicleType.TRUCK, new BigDecimal("50")
    );

    @Override
    public BigDecimal calculateParkingFee(Ticket ticket){
        BigDecimal price = HOURLY_RATES.getOrDefault(ticket.getVehicle().getVehicleType(), new BigDecimal("0"));
        long minutes = ChronoUnit.MINUTES.between(ticket.getEntryTime(), ticket.getExitTime());
        long hours = (long)Math.ceil(minutes/60.0);
        price = price.multiply(new BigDecimal(hours));
        return price;
    }
}
