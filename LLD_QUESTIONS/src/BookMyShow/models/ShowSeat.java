package BookMyShow.models;

import BookMyShow.enums.ShowSeatStatus;

public class ShowSeat {
    private final Long showId;
    private final Long seatId;
    private ShowSeatStatus showSeatStatus;

    public ShowSeat(Long showId, Long seatId) {
        this.showId = showId;
        this.seatId = seatId;
        this.showSeatStatus = ShowSeatStatus.AVAILABLE;
    }

    public Long getShowId() {
        return showId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public ShowSeatStatus getShowSeatStatus() {
        return showSeatStatus;
    }

    public void setShowSeatStatus(ShowSeatStatus showSeatStatus){
        this.showSeatStatus = showSeatStatus;
    }
}
