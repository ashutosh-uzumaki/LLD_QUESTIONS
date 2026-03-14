package ParkingLot.models;

import ParkingLot.enums.VehicleType;

public class Vehicle {
    private final String licensePlate;
    private final VehicleType vehicleType;

    public Vehicle(String licensePlate, VehicleType vehicleType){
        if(licensePlate == null || licensePlate.trim().isEmpty() || vehicleType == null){
            throw new IllegalArgumentException("Vehicle details are not valid");
        }
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
