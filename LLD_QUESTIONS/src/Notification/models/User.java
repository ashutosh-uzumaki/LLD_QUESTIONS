package Notification.models;

import Notification.enums.NotificationChannel;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final String id;
    private final Map<NotificationChannel, String> channelAddress;

    public User(String id){
        this.id = id;
        this.channelAddress = new HashMap<>();
    }

    public void setChannelAddress(NotificationChannel channel, String token){
        channelAddress.put(channel, token);
    }

    public String getId() {
        return id;
    }

    public String getChannelAddress(NotificationChannel channel) {
        return channelAddress.get(channel);
    }
}
