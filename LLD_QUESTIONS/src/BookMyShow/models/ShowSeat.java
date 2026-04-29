package BookMyShow.models;

import BookMyShow.enums.ShowSeatStatus;

public class ShowSeat {
    private final Long showSeatId;
    private final Long showId;
    private final Long seatId;
    private ShowSeatStatus showSeatStatus;

    public ShowSeat(Long showSeatId, Long showId, Long seatId) {
        this.showSeatId = showSeatId;
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

    public Long getShowSeatId(){
        return showSeatId;
    }

    public ShowSeatStatus getShowSeatStatus() {
        return showSeatStatus;
    }

    public void setShowSeatStatus(ShowSeatStatus showSeatStatus){
        this.showSeatStatus = showSeatStatus;
    }


}
