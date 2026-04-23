package UrlShortener.repository;

import UrlShortener.models.UrlRecord;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository implements UrlRepository{
    private final ConcurrentHashMap<String, UrlRecord> urlRecords = new ConcurrentHashMap<>();
    @Override
    public void save(UrlRecord record) {
        urlRecords.putIfAbsent(record.getShortUrl(), record);
    }

    @Override
    public Optional<UrlRecord> findByShortUrl(String shortUrl) {
        return Optional.ofNullable(urlRecords.get(shortUrl));
    }
}
