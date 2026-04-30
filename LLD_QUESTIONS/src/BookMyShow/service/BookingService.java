package BookMyShow.service;

import BookMyShow.models.Show;
import BookMyShow.models.ShowSeat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingService {
    private final SeatLockingService seatLockingService;
    private final ShowService showService;

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
}
