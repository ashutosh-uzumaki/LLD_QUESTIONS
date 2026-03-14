package ParkingLot.models;

import ParkingLot.enums.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final List<Floor> floors;
    private Map<String, Ticket> tickets = new HashMap<>();

    public ParkingLot(List<Floor> floors){
        this.floors = floors;
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

    public void unparkVehicle(String ticketId){
        Ticket ticket = tickets.get(ticketId);
        ParkingSpot spot = ticket.getParkingSpot();
        spot.unparkVehicle();
        ticket.setExitTime();
        tickets.remove(ticketId);
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
