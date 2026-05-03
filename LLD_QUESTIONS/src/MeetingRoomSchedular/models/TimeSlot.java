package MeetingRoomSchedular.models;

import java.time.LocalDateTime;

public class TimeSlot {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

    public boolean isOverlap(TimeSlot other){
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}
