package com.vnn.urlshortener.service;

import com.vnn.urlshortener.entity.Url;
import com.vnn.urlshortener.repository.UrlRepository;
import com.vnn.urlshortener.service.impl.UrlServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceImplTest {
    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    @Test
    void createShortUrl_shouldReturnShortCode() {
        // given
        String originalUrl = "https://google.com";
        when(urlRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // when
        String result = urlService.createShortUrl(originalUrl);

        // then
        assertThat(result).isNotBlank();
        assertThat(result).hasSize(7);
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void getOriginalUrl_shouldReturnOriginalUrl() {
        String shortCode = "abc1234";
        String originalUrl = "https://google.com";
        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .build();
        when(urlRepository.findByShortCode(shortCode)).thenReturn(java.util.Optional.of(url));

        // when
        String result = urlService.getOriginalUrl(shortCode);

        // then
        assertThat(result).isEqualTo(originalUrl);
        verify(urlRepository).findByShortCode(shortCode);
    }

    @Test
    void getOriginalUrl_shouldThrowExceptionWhenShortCodeNotFound() {
        String shortCode = "notfound";
        when(urlRepository.findByShortCode(shortCode)).thenReturn(java.util.Optional.empty());

        // when
        Throwable thrown = org.assertj.core.api.Assertions.catchThrowable(() -> urlService.getOriginalUrl(shortCode));

        // then
        assertThat(thrown).isInstanceOf(com.vnn.urlshortener.exception.UrlNotFoundException.class)
                .hasMessageContaining("Short code not found: " + shortCode);
        verify(urlRepository).findByShortCode(shortCode);
    }
}
