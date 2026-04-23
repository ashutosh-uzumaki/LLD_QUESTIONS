package UrlShortener.repository;

import UrlShortener.models.UrlRecord;

import java.util.Optional;

public interface UrlRepository {
    void save(UrlRecord record);
    Optional<UrlRecord> findByShortUrl(String shortUrl);
}
