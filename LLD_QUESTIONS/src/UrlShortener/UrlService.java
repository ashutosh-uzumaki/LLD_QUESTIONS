package UrlShortener;

import UrlShortener.models.UrlRecord;
import UrlShortener.repository.UrlRepository;
import UrlShortener.strategy.UrlShortenerStrategy;

import java.time.LocalDate;
import java.util.Optional;

public class UrlService {
    private final UrlRepository urlRepository;
    private UrlShortenerStrategy urlShortenerStrategy;

    public UrlService(UrlRepository urlRepository, UrlShortenerStrategy urlShortenerStrategy){
        this.urlRepository = urlRepository;
        this.urlShortenerStrategy = urlShortenerStrategy;
    }

    public void setUrlShortenerStrategy(UrlShortenerStrategy urlShortenerStrategy) {
        this.urlShortenerStrategy = urlShortenerStrategy;
    }

    public String shortenUrl(String longUrl){
        String shortUrl = urlShortenerStrategy.shortenUrl(longUrl);
        UrlRecord record = new UrlRecord(longUrl, shortUrl, LocalDate.now(), LocalDate.now().plusYears(2));
        urlRepository.save(record);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl){
        Optional<UrlRecord> record = urlRepository.findByShortUrl(shortUrl);
        return record
                .filter(r -> !r.isExpired())
                .map(UrlRecord::getLongUrl)
                .orElse(null);
    }
}
