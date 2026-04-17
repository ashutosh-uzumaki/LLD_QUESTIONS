package RateLimiter.models;

public class ClientConfig {
    private final int maxRequests;
    private final long windowSizeMs;

    public ClientConfig(int maxRequests, long windowSizeMs){
        if(maxRequests <= 0){
            throw new IllegalArgumentException("Max requests should be positive");
        }
        if(windowSizeMs <= 0){
            throw new IllegalArgumentException("Window size should be positive");
        }
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeMs() {
        return windowSizeMs;
    }
}
