package ParkingLot.models;

import ParkingLot.enums.VehicleType;
import ParkingLot.strategy.PaymentStrategy;
import ParkingLot.strategy.PricingStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final List<Floor> floors;
    private Map<String, Ticket> tickets = new HashMap<>();
    private final PricingStrategy pricingStrategy;
    public ParkingLot(List<Floor> floors, PricingStrategy pricingStrategy){
        this.floors = floors;
        this.pricingStrategy = pricingStrategy;
    }

    public Ticket parkVehicle(Vehicle vehicle){
        for(Floor floor: floors){
            ParkingSpot spot = floor.findParkingSpot(vehicle.getVehicleType());
            if (spot != null) {
                spot.parkVehicle(vehicle);
                Ticket ticket = new Ticket(vehicle, spot);
                tickets.put(ticket.getTicketId(), ticket);
                return ticket;
            }
        }
        return null;
    }

    public PaymentReceipt unparkVehicle(String ticketId){
        Ticket ticket = tickets.get(ticketId);
        ParkingSpot spot = ticket.getParkingSpot();
        spot.unparkVehicle();
        ticket.setExitTime();
        BigDecimal amount = pricingStrategy.calculateParkingFee(ticket);
        tickets.remove(ticketId);
        return new PaymentReceipt(ticket, amount);
    }

    public boolean hasAvailableSpot(VehicleType vehicleType){
        for(Floor floor: floors){
            ParkingSpot spot = floor.findParkingSpot(vehicleType);
            if(spot != null){
                return true;
            }
        }
        return false;
    }
}
