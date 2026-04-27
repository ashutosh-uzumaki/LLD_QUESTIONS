package BookMyShow.models;

import BookMyShow.enums.BookingStatus;
import BookMyShow.enums.ShowSeatStatus;
import BookMyShow.service.PaymentMethod;

import java.util.List;

public class Booking {
    private final Long bookingId;
    private final Long userId;
    private BookingStatus bookingStatus;
    private final List<ShowSeat> showSeatList;
    private final PaymentMethod paymentMethod;

    public Booking(Long bookingId, Long userId, BookingStatus bookingStatus, List<ShowSeat> showSeatList, PaymentMethod paymentMethod){
        this.bookingId = bookingId;
        this.userId = userId;
        this.bookingStatus = bookingStatus;
        this.showSeatList = showSeatList;
        this.paymentMethod = paymentMethod;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public List<ShowSeat> getShowSeatList() {
        return showSeatList;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


}
