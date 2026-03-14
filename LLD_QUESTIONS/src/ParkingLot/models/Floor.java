package ParkingLot.models;

import ParkingLot.enums.VehicleType;

import java.util.*;

public class Floor {
    private final String floorNumber;
    private final List<ParkingSpot> parkingSpots;

    public Floor(String floorNumber, List<ParkingSpot> parkingSpots) {
        this.floorNumber = floorNumber;
        this.parkingSpots = parkingSpots;
    }

    public ParkingSpot findParkingSpot(VehicleType vehicleType) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isAvailable() && vehicleType.canFitIn(spot.getSpotSize())) {
                return spot;
            }
        }
        return null;
    }

    public List<ParkingSpot> findAvailableSpots() {
        List<ParkingSpot> availableSpots = new ArrayList<>();
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isAvailable()) {
                availableSpots.add(spot);
            }
        }
        return availableSpots;
    }
}
