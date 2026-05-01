package BookMyShow;

import BookMyShow.enums.AudioType;
import BookMyShow.enums.ScreenType;
import BookMyShow.enums.SeatTier;
import BookMyShow.models.*;
import BookMyShow.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookMyShowDemo {
    public static void main(String[] args) {
        User user = new User(1L, "Ashu", "6384137446", "ashutoshdev16@gmail.com");
        User user2 = new User(2L, "Ankita", "8248227255", "ashutosh.dev16@gmail.com");
        Theatre theatre = new Theatre(1L, "PVR", "Bengaluru", "1/12 Sarjapur Road, Bengaluru, Karnataka");
        Screen screen = new Screen(1L, "A1", ScreenType.S2D, AudioType.DOLBY_ATMOS, theatre.getTheatreId());
        Screen screen2 = new Screen(2L, "A2", ScreenType.S3D, AudioType.DOLBY_ATMOS, theatre.getTheatreId());
        Movie movie = new Movie(1L, "King Kong", 120, "Action");
        Show morningShow = new Show(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(120));
        List<ShowSeat> showSeats = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
            Seat seat = new Seat((long)i, "A"+i, SeatTier.GOLD, screen.getScreenId());
            ShowSeat showSeat = new ShowSeat((long)i, morningShow.getShowId(), seat.getSeatId(), new BigDecimal("500"));
            showSeats.add(showSeat);
        }
        ShowService showService = new ShowService();
        showService.addSeat(1L, showSeats);
        SeatLockingService seatLockingService = new SeatLockingService();
        BookingService bookingService = new BookingService(showService, seatLockingService);
        List<ShowSeat> selectedSeats = List.of(showSeats.get(0), showSeats.get(1));
        if(bookingService.selectSeats(user.getUserId(), 1L, selectedSeats)){
            Booking booking = bookingService.createBooking(user.getUserId(), 1L, selectedSeats);
            PaymentMethod paymentMethod = new UPIPayment();
            if(bookingService.makePayment(booking.getBookingId(), user.getUserId(), paymentMethod)){
                System.out.println("Booking Successful");
            }else{
                System.out.println("Booking Failed");
            }
        }else{
            System.out.println("Please Select Another Seats. Few Seats are being held by other users");
        }

        List<ShowSeat> concurrentSeats = List.of(showSeats.get(2), showSeats.get(3));
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            // User 1 flow
            if(bookingService.selectSeats(user.getUserId(), 1L, concurrentSeats)){
                Booking booking = bookingService.createBooking(user.getUserId(), 1L, concurrentSeats);
                bookingService.makePayment(booking.getBookingId(), user.getUserId(), new UPIPayment());
                System.out.println("User 1 - Booking Successful!");
            } else {
                System.out.println("User 1 - Seats not available!");
            }
        });

        executor.submit(() -> {
            // User 2 - same seats!
            if(bookingService.selectSeats(user2.getUserId(), 1L, concurrentSeats)){
                Booking booking = bookingService.createBooking(user2.getUserId(), 1L, concurrentSeats);
                bookingService.makePayment(booking.getBookingId(), user2.getUserId(), new UPIPayment());
                System.out.println("User 2 - Booking Successful!");
            } else {
                System.out.println("User 2 - Seats not available!");
            }
        });

        executor.shutdown();
    }
}
