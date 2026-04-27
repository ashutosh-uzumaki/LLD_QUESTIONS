package BookMyShow.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Show {
    private final Long movieId;
    private final Long screenId;
    private final BigDecimal price;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Show(Long movieId, Long screenId, BigDecimal price, LocalDateTime startTime, LocalDateTime endTime) {
        this.movieId = movieId;
        this.screenId = screenId;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
