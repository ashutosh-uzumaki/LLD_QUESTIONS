package BookMyShow.models;

import BookMyShow.enums.ShowSeatStatus;

import java.math.BigDecimal;

public class ShowSeat {
    private final Long showSeatId;
    private final Long showId;
    private final Long seatId;
    private ShowSeatStatus showSeatStatus;
    private final BigDecimal price;

    public ShowSeat(Long showSeatId, Long showId, Long seatId, BigDecimal price) {
        this.showSeatId = showSeatId;
        this.showId = showId;
        this.seatId = seatId;
        this.showSeatStatus = ShowSeatStatus.AVAILABLE;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }
}
