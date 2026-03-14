package ParkingLot.factory;

import ParkingLot.enums.VehicleType;
import ParkingLot.models.Vehicle;

public class VehicleFactory {
    public static Vehicle createVehicle(String licensePlate, String vehicleType){
        vehicleType = vehicleType.toUpperCase().trim();
        switch (vehicleType){
            case "CAR" : return new Vehicle(licensePlate, VehicleType.CAR);
            case "BIKE": return new Vehicle(licensePlate, VehicleType.BIKE);
            case "TRUCK": return new Vehicle(licensePlate, VehicleType.TRUCK);
            default:
                throw new RuntimeException("No such vehicle exists!!");
        }
    }
}
