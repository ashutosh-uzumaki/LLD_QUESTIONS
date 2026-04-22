package Notification.service;

import Notification.enums.NotificationChannel;
import Notification.models.NotificationRequest;

public interface Notification {
    String getRecipientAddress();
    String getMessage();
    NotificationChannel getChannel();
}
