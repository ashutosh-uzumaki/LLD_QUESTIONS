package ParkingLot;

import ParkingLot.enums.SpotSize;
import ParkingLot.enums.VehicleType;
import ParkingLot.models.*;
import ParkingLot.strategy.HourlyPricingStrategy;
import ParkingLot.strategy.PricingStrategy;

import java.util.ArrayList;
import java.util.List;

public class ParkingService {
    public static void main(String[] args) {
        PricingStrategy pricingStrategy = new HourlyPricingStrategy();
        ParkingSpot spot = new ParkingSpot("A01", SpotSize.SMALL);
        ParkingSpot spot2 = new ParkingSpot("A02", SpotSize.LARGE);
        List<ParkingSpot> spots = List.of(spot, spot2);
        Floor floor1 = new Floor("F1", spots);
        List<Floor> floors = List.of(floor1);
        ParkingLot parkLot = new ParkingLot(floors, pricingStrategy);

        Vehicle v1 = new Vehicle("KA-01-AB-231", VehicleType.CAR);
        Ticket t = parkLot.parkVehicle(v1);
        if(t == null){
            System.out.println("Sorry!!! No available spots");
        }

        Vehicle v2 = new Vehicle("KA-01-AB-345", VehicleType.BIKE);
        Ticket t1 = parkLot.parkVehicle(v2);
        if(t1 == null){
            System.out.println("Sorry!!! No available spots");
        }else{
            System.out.println("Your vehicle is parked at: "+t1.getParkingSpot().getSpotNumber());
        }
        PaymentReceipt receipt = parkLot.unparkVehicle(t1.getTicketId());
    }
}
