package RateLimiter.strategy;

import RateLimiter.models.ClientConfig;

public interface RateLimiterStrategy {
    boolean isAllowed(String clientId, ClientConfig config);
}
