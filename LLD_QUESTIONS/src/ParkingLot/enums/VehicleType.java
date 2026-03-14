package ParkingLot.enums;

import ParkingLot.models.Vehicle;

import java.util.List;
import java.util.Set;

public enum VehicleType {
    BIKE(Set.of(SpotSize.SMALL)),
    CAR(Set.of(SpotSize.MEDIUM)),
    TRUCK(Set.of(SpotSize.LARGE));

    private final Set<SpotSize> allowedSpotSizes;

    VehicleType(Set<SpotSize> allowedSpotSizes){
        this.allowedSpotSizes = allowedSpotSizes;
    }

    public boolean canFitIn(SpotSize spotSize){
        return allowedSpotSizes.contains(spotSize);
    }
}
