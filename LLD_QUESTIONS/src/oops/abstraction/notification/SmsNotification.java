package oops.abstraction.notification;

import java.time.LocalDateTime;

public class SmsNotification extends Notification{
    public SmsNotification(String recepient, String message, LocalDateTime timeStamp){
        super(recepient, message, timeStamp);
    }


    @Override
    public boolean validateNotification(){
        if(getRecepient() == null || getRecepient().trim().isEmpty() || getRecepient().length() < 13){
            return false;
        }

        if(getMessage() == null || getMessage().trim().isEmpty()){
            return false;
        }

        return true;
    }

    @Override
    public boolean deliverNotification(){
        System.out.println("Notification sent via SMS!!");
        return true;
    }

}
