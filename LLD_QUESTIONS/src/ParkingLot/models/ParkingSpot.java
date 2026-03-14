package ParkingLot.models;

import ParkingLot.enums.SpotSize;

public class ParkingSpot {
    private final String spotNumber;
    private final SpotSize spotSize;
    private Vehicle vehicle;

    public ParkingSpot(String spotNumber, SpotSize spotSize){
        this.spotNumber = spotNumber;
        this.spotSize = spotSize;
    }

    public void parkVehicle(Vehicle vehicle){
        if(!isAvailable()){
          throw new IllegalArgumentException("This spot is already occupied");
        }
        this.vehicle = vehicle;
    }

    public void unparkVehicle(){
        if(isAvailable()){
            throw new IllegalArgumentException("This spot is already empty");
        }
        this.vehicle = null;
    }

    public boolean isAvailable(){
        return vehicle == null;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public SpotSize getSpotSize() {
        return spotSize;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
