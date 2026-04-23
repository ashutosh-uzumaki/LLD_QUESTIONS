package UrlShortener.models;

import java.time.LocalDate;

public class UrlRecord {
    private final String longUrl;
    private final String shortUrl;
    private final LocalDate createdAt;
    private LocalDate expiryAt;

    public UrlRecord(String longUrl, String shortUrl, LocalDate createdAt, LocalDate expiryAt){
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDate date){
        expiryAt = date;
    }

    public boolean isExpired(){
        return expiryAt.isBefore(LocalDate.now());
    }
}
