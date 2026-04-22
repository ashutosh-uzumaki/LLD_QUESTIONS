package Notification.notifications;

import Notification.enums.NotificationChannel;
import Notification.service.Notification;

public class IOSPushNotification implements Notification {
    @Override
    public String getRecipientAddress() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public NotificationChannel getChannel() {
        return null;
    }
}