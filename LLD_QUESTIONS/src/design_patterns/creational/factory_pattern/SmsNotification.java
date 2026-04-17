package design_patterns.creational.factory_pattern;

public class SmsNotification implements Notification{
    @Override
    public void send(String message){
        System.out.println("SMS Sent!!");
    }
}
