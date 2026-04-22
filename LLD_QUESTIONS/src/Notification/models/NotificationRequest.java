package Notification.models;

import Notification.enums.NotificationChannel;
import Notification.enums.NotificationType;

import java.util.List;

public class NotificationRequest {
    private final String senderId;
    private final String receiverId;
    private final String message;
    private final List<NotificationChannel> channels;
    private final NotificationType notificationType;

    public NotificationRequest(String senderId, String receiverId, String message, List<NotificationChannel> channels, NotificationType notificationType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.channels = channels;
        this.notificationType = notificationType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public List<NotificationChannel> getChannels() {
        return channels;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
