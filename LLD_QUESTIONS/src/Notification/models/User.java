package Notification.models;

import Notification.enums.NotificationChannel;
import Notification.enums.NotificationType;

import java.util.*;

public class User {
    private final int id;
    private final String name;
    private Map<NotificationChannel, String> channelAddress;
    private Map<NotificationType, Set<NotificationChannel>> subscriptionMapping;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.channelAddress = new HashMap<>();
        this.subscriptionMapping = new HashMap<>();
    }

    public void setChannelAddress(NotificationChannel channel, String address){
        channelAddress.put(channel, address);
    }

    public boolean optIn(NotificationType notificationType, List<NotificationChannel> channels){
        Set<NotificationChannel> existingChannels = subscriptionMapping.getOrDefault(notificationType, new HashSet<>());
        existingChannels.addAll(channels);
        subscriptionMapping.put(notificationType, existingChannels);
        return true;
    }

    public boolean optOut(NotificationType notificationType, List<NotificationChannel> channels){
        Set<NotificationChannel> existingChannels = subscriptionMapping.getOrDefault(notificationType, new HashSet<>());
        channels.forEach(existingChannels::remove);
        subscriptionMapping.put(notificationType, existingChannels);
        return true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChannelAddress(NotificationChannel channel) {
        return channelAddress.get(channel);
    }

    public List<NotificationChannel> getSubscriptionMapping(NotificationType notificationType) {
        return subscriptionMapping.get(notificationType).stream().toList();
    }
}
