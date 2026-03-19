package com.vnn.urlshortener.controller;

import com.vnn.urlshortener.dto.request.ShortenRequest;
import com.vnn.urlshortener.dto.response.ShortenResponse;
import com.vnn.urlshortener.dto.response.StatsResponse;
import com.vnn.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/api/shorten")
    public ShortenResponse shorten(@Valid @RequestBody ShortenRequest request){
        String shortCode = urlService.createShortUrl(request.getUrl());
        return new ShortenResponse("http://localhost:8080/" + shortCode);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<String> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginalUrl(shortCode);
        urlService.incrementClickCount(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<StatsResponse> getStats(@PathVariable String shortCode){
        Long clickCount = urlService.getClickCount(shortCode);
        StatsResponse response = new StatsResponse(shortCode, clickCount);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode){
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}
