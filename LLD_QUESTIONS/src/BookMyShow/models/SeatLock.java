package BookMyShow.models;

import java.time.LocalDateTime;

public class SeatLock {
    private final Long userId;
    private final Long showSeatId;
    private final LocalDateTime lockUntil;

    public SeatLock(Long userId, Long showSeatId, LocalDateTime lockUntil){
        this.userId = userId;
        this.showSeatId = showSeatId;
        this.lockUntil = lockUntil;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getShowSeatId() {
        return showSeatId;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }
}
