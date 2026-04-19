package RateLimiter;

import RateLimiter.models.ClientConfig;

public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig(3, 1000);
        String clientId = "A1";
        RateLimiter limiter = RateLimiter.getInstance();
        limiter.registerClient(clientId, clientConfig);
        for(int i = 1; i <= 5; i++) {
            System.out.println("Request " + i + ": " + (limiter.isAllowed(clientId) ? "ALLOWED" : "BLOCKED"));
        }
        clientId = "A2";
        System.out.println("Request : " + (limiter.isAllowed(clientId) ? "ALLOWED" : "BLOCKED"));
    }
}
