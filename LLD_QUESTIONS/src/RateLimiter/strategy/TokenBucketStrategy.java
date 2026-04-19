package RateLimiter.strategy;

import RateLimiter.models.ClientConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketStrategy implements RateLimiterStrategy{
    private final ConcurrentHashMap<String, TokenBucketState> stateMap = new ConcurrentHashMap<>();
    private static class TokenBucketState{
        private int tokens;
        private long lastRefillTime;
        private final ReentrantLock lock = new ReentrantLock();

        public TokenBucketState(int capacity, long now){
            this.tokens = capacity;
            this.lastRefillTime = now;
        }
    }

    @Override
    public boolean isAllowed(String clientId, ClientConfig config) {
        TokenBucketState state = stateMap.computeIfAbsent(clientId, k -> new TokenBucketState(config.getMaxRequests(), System.currentTimeMillis()));
        state.lock.lock();
        try{
            long elapsedTime = System.currentTimeMillis() - state.lastRefillTime;
            int tokensToBeAdded = (int)(elapsedTime / config.getWindowSizeMs()) * config.getMaxRequests();
            if(tokensToBeAdded > 0) {
                state.tokens = Math.min(config.getMaxRequests(), state.tokens + tokensToBeAdded);
                state.lastRefillTime = System.currentTimeMillis();
            }
            if(state.tokens > 0){
                state.tokens --;
                return true;
            }
            return false;
        }finally {
            state.lock.unlock();
        }
    }

}
