package com.vnn.urlshortener.service;

public interface UrlService {
    String createShortUrl(String originalUrl);
    String getOriginalUrl(String shortCode);
    void incrementClickCount(String shortCode);
    void deleteUrl(String shortCode);
    Long getClickCount(String shortCode);
}
