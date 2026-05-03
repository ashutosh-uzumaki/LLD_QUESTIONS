package MeetingRoomSchedular.models;

import MeetingRoomSchedular.enums.MeetingStatus;

import java.util.*;

public class Meeting {
    private final String id;
    private final User organizer;
    private final List<User> participants;
    private final Room room;
    private final TimeSlot slot;
    private final String subject;
    private MeetingStatus meetingStatus;

    public Meeting(String id, User organizer, List<User> participants, Room room, TimeSlot slot, String subject){
        this.id = id;
        this.organizer = organizer;
        this.participants = new ArrayList<>(participants);
        this.room = room;
        this.slot = slot;
        this.meetingStatus = MeetingStatus.SCHEDULED;
        this.subject = subject;
    }

    public List<User> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public Room getRoom() {
        return room;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public MeetingStatus getMeetingStatus() {
        return meetingStatus;
    }

    public String getId() {
        return id;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void completeMeeting(){
        if(meetingStatus != MeetingStatus.SCHEDULED){
            throw new IllegalArgumentException("Cannot complete a non-scheduled meeting");
        }
        meetingStatus = MeetingStatus.COMPLETED;
    }

    public void cancelMeeting(){
        if(meetingStatus != MeetingStatus.SCHEDULED){
            throw new IllegalArgumentException("Cannot cancel a non-scheduled meeting");
        }
        meetingStatus = MeetingStatus.CANCELLED;
    }

    public String getSubject(){
        return subject;
    }
}
