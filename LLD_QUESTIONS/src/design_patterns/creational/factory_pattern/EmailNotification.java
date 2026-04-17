package design_patterns.creational.factory_pattern;

public class EmailNotification implements Notification{
    @Override
    public void send(String message){
        System.out.println("Email Sent");
    }
}
