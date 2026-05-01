package BookMyShow.models;

import BookMyShow.enums.SeatTier;

public class Seat {
    private final Long seatId;
    private final String name;
    private final SeatTier seatTier;
    private final Long screenId;

    public Seat(Long seatId, String name, SeatTier seatTier, Long screenId){
        this.seatId = seatId;
        this.name = name;
        this.seatTier = seatTier;
        this.screenId = screenId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public String getName() {
        return name;
    }

    public SeatTier getSeatTier() {
        return seatTier;
    }

    public Long getScreenId() {
        return screenId;
    }
}
