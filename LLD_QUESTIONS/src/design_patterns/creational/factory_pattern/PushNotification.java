package design_patterns.creational.factory_pattern;

public class PushNotification implements Notification{
    @Override
    public void send(String message){
        System.out.println("Push Notification Sent!!");
    }
}
