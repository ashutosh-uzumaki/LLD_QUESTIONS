package UrlShortener.strategy;

import java.util.concurrent.atomic.AtomicLong;

public class Base62EncodingStrategy implements UrlShortenerStrategy{
    private AtomicLong counter = new AtomicLong(1);
    private static final String BASE_62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Override
    public String shortenUrl(String longUrl){
        long id = counter.getAndIncrement();
        StringBuilder sb = new StringBuilder();
        while(id > 0){
            long remainder = id % 62;
            sb.append(BASE_62.charAt((int) remainder));
            id /= 62;
        }
        return sb.reverse().toString();
    }
}
