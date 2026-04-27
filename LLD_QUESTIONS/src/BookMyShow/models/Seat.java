package BookMyShow.models;

public class Seat {
    private final Long seatId;
    private final String name;
    private final Long screenId;

    public Seat(Long seatId, String name, Long screenId){
        this.seatId = seatId;
        this.name = name;
        this.screenId = screenId;
    }
}
