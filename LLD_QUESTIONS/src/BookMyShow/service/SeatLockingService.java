package BookMyShow.service;

import BookMyShow.enums.ShowSeatStatus;
import BookMyShow.models.Seat;
import BookMyShow.models.SeatLock;
import BookMyShow.models.ShowSeat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SeatLockingService {
    private final ConcurrentHashMap<Long, SeatLock> seatLocks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ShowSeat> showSeatsMap = new ConcurrentHashMap<>();
    private static final Integer LOCK_DURATION = 10;
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    public boolean lockSeats(Long userId, List<ShowSeat> showSeats){
        for(ShowSeat showSeat: showSeats){
            Long showSeatId = showSeat.getShowSeatId();
            if(showSeat.getShowSeatStatus().name().equals("BOOKED")){
                return false;
            }
            if(!showSeat.getShowSeatStatus().name().equals("AVAILABLE")){
                SeatLock currLock = seatLocks.get(showSeatId);
                if(!validateLock(userId, currLock)){
                    return false;
                }
            }
            showSeatsMap.put(showSeatId, showSeat);
            LocalDateTime newLockUntil = LocalDateTime.now().plusMinutes(LOCK_DURATION);
            SeatLock newLock = new SeatLock(userId, showSeatId, newLockUntil);
            showSeat.setShowSeatStatus(ShowSeatStatus.HELD);
            seatLocks.put(showSeatId, newLock);
            scheduledExecutorService.schedule(
                    () -> releaseLock(showSeatId, showSeatsMap.get(showSeatId)),
                    LOCK_DURATION,
                    TimeUnit.MINUTES
            );

        }
        return true;
    }

    public boolean validateLock(Long userId, SeatLock currLock){
        Long lockedUserId = currLock.getUserId();
        if(!Objects.equals(lockedUserId, userId) && currLock.getLockUntil().isAfter(LocalDateTime.now())){
            return false;
        }
        return true;
    }

    public void releaseLock(Long seatId, ShowSeat showSeat){
        seatLocks.remove(seatId);
        showSeatsMap.remove(seatId);
        showSeat.setShowSeatStatus(ShowSeatStatus.AVAILABLE);
    }
}
