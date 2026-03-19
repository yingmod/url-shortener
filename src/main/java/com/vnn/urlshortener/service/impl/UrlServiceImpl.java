package com.vnn.urlshortener.service.impl;

import com.vnn.urlshortener.entity.Url;
import com.vnn.urlshortener.exception.UrlNotFoundException;
import com.vnn.urlshortener.repository.UrlRepository;
import com.vnn.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;

    @Override
    public String createShortUrl(String originalUrl) {
        String shortCode = UUID.randomUUID().toString().substring(0, 7);
        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .build();
        urlRepository.save(url);
        return shortCode;
    }

    @Cacheable(value = "urls", key = "#shortCode")
    @Override
    public String getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short code not found: " + shortCode));
        return url.getOriginalUrl();
    }

    @Override
    public void incrementClickCount(String shortCode) {
        urlRepository.findByShortCode(shortCode).ifPresent(url -> {
            url.setClickCount(url.getClickCount() + 1);
            urlRepository.save(url);
        });
    }


    @CacheEvict(value = "urls", key = "#shortCode")
    @Override
    public void deleteUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short code not found: " + shortCode));
        urlRepository.delete(url);
    }

    @Override
    public Long getClickCount(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short code not found: " + shortCode));
        return url.getClickCount();
    }
}
