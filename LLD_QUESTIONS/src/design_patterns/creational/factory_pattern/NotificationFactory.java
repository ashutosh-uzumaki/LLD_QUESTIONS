package design_patterns.creational.factory_pattern;

public class NotificationFactory {
    public static Notification create(String notificationType){
        if(notificationType.equalsIgnoreCase("sms")){
            return new SmsNotification();
        }else if(notificationType.equalsIgnoreCase("email")){
            return new EmailNotification();
        }else if(notificationType.equalsIgnoreCase("push")){
            return new PushNotification();
        }else{
            throw new IllegalArgumentException("This notification type is not supported");
        }
    }
}
