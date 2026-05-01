package BookMyShow.service;

import BookMyShow.enums.BookingStatus;
import BookMyShow.enums.ShowSeatStatus;
import BookMyShow.models.Booking;
import BookMyShow.models.Show;
import BookMyShow.models.ShowSeat;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookingService {
    private final SeatLockingService seatLockingService;
    private final ShowService showService;
    private final ConcurrentHashMap<Long, List<Booking>> bookings = new ConcurrentHashMap<>();

    public BookingService(ShowService showService, SeatLockingService seatLockingService){
        this.showService = showService;
        this.seatLockingService = seatLockingService;
    }

    public boolean selectSeats(Long userId, Long showId, List<ShowSeat> selectedSeats){
        Set<Long> availableSeats = showService.getSeats(showId).stream().map(ShowSeat::getShowSeatId).collect(Collectors.toSet());
        for(ShowSeat showSeat: selectedSeats){
            if(!availableSeats.contains(showSeat.getShowSeatId())){
                return false;
            }
        }
        return seatLockingService.lockSeats(userId, selectedSeats);
    }

    public Booking createBooking(Long userId, Long showId, List<ShowSeat> selectedSeats){
        BigDecimal totalAmount = selectedSeats.stream().map(ShowSeat::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        Booking booking = new Booking(
                UUID.randomUUID().getLeastSignificantBits(),
                userId,
                showId,
                BookingStatus.IN_PROGRESS,
                selectedSeats,
                null,
                totalAmount
        );
        bookings.computeIfAbsent(userId, k->new ArrayList<>()).add(booking);
        return booking;
    }

    public boolean makePayment(Long bookingId, Long userId, PaymentMethod paymentMethod){
        Booking currBooking  = bookings.get(userId).stream().filter(booking -> booking.getBookingId().equals(bookingId)).findFirst().orElseThrow(() -> new RuntimeException("No Bookings found"));
        currBooking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        currBooking.setPaymentMethod(paymentMethod);
        try{
            paymentMethod.pay(currBooking.getAmount());
            currBooking.setBookingStatus(BookingStatus.BOOKED);
            for(ShowSeat showSeat: currBooking.getShowSeatList()){
                showSeat.setShowSeatStatus(ShowSeatStatus.BOOKED);
            }
        } catch (Exception e) {
            currBooking.setBookingStatus(BookingStatus.FAILED);
            for(ShowSeat showSeat: currBooking.getShowSeatList()){
                showSeat.setShowSeatStatus(ShowSeatStatus.AVAILABLE);
                seatLockingService.releaseLock(showSeat.getShowSeatId(), showSeat);
            }
        }
        return true;
    }
}
