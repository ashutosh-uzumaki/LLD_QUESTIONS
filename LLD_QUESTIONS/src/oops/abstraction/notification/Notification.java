package oops.abstraction.notification;

import java.time.LocalDateTime;

public abstract class Notification {
    private final String recepient;
    private final String message;
    private final LocalDateTime timeStamp;

    public Notification(String recepient, String message, LocalDateTime timeStamp){
        this.recepient = recepient;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getRecepient(){
        return recepient;
    }
    public String getMessage(){
        return message;
    }
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }

    public void send(){
        if(!validateNotification()){
            throw new IllegalArgumentException("Validation Failed")
        }
        deliverNotification();
    }

    public abstract boolean validateNotification();
    public abstract boolean deliverNotification();
}
