package BookMyShow.models;

import java.math.BigDecimal;

public class Ticket {
    private final Long ticketId;
    private final Long bookingId;
    private final Long showSeatId;
    private final Long showId;
    private final BigDecimal amount;

    public Ticket(Long ticketId, Long bookingId, Long showSeatId, Long showId, BigDecimal amount) {
        this.ticketId = ticketId;
        this.bookingId = bookingId;
        this.showSeatId = showSeatId;
        this.showId = showId;
        this.amount = amount;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getShowSeatId() {
        return showSeatId;
    }

    public Long getShowId() {
        return showId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
