package RateLimiter;

import RateLimiter.models.ClientConfig;
import RateLimiter.strategy.RateLimiterStrategy;
import RateLimiter.strategy.TokenBucketStrategy;

import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final ConcurrentHashMap<String, ClientConfig> configMap;
    private final RateLimiterStrategy rateLimiterStrategy;
    private RateLimiter(){
        this.configMap = new ConcurrentHashMap<>();
        this.rateLimiterStrategy = new TokenBucketStrategy();
    }

    private static class Holder{
        private static final RateLimiter INSTANCE = new RateLimiter();
    }

    public static RateLimiter getInstance(){
        return Holder.INSTANCE;
    }

    public void registerClient(String clientId, ClientConfig config){
        configMap.putIfAbsent(clientId, config);
    }

    public boolean isAllowed(String clientId){
        ClientConfig config = configMap.get(clientId);
        if(config == null){
            throw new IllegalArgumentException("Client not registered: " + clientId);
        }
        return rateLimiterStrategy.isAllowed(clientId, config);
    }
}
