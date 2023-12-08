package com.kt.urlshortener.service

import com.kt.urlshortener.controller.model.OriginalUrlResponse
import com.kt.urlshortener.controller.model.UrlShorteningRequest
import com.kt.urlshortener.db.UrlMapping
import com.kt.urlshortener.exception.UrlInvalidException
import com.kt.urlshortener.exception.UrlNotFoundException
import com.kt.urlshortener.repository.UrlMappingRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
class UrlServiceTest {

    @Mock
    lateinit var urlMappingRepository: UrlMappingRepository

    @InjectMocks
    lateinit var urlService: UrlService

    @Test
    fun shortenUrlForNonExistingShortUrlIsSuccessful() {
        val expectedShortUrl = "c5cd9fc1"
        val urlShorteningRequest = UrlShorteningRequest("https://google.com")
        val argumentCaptor: ArgumentCaptor<UrlMapping> = ArgumentCaptor.forClass(UrlMapping::class.java)

        whenever(urlMappingRepository.findByShortUrl(any())).thenReturn(null)

        val urlShorteningResponse = urlService.shortenUrl(urlShorteningRequest)

        assertNotNull(urlShorteningResponse)
        assertNotNull(urlShorteningResponse.shortenedUrl)
        assertEquals(expectedShortUrl, urlShorteningResponse.shortenedUrl)
        Mockito.verify(urlMappingRepository, times(1)).findByShortUrl(expectedShortUrl)
        Mockito.verify(urlMappingRepository, times(1)).save(argumentCaptor.capture())

        val capturedUrlMapping: UrlMapping? = argumentCaptor.value
        assertEquals(expectedShortUrl, capturedUrlMapping?.shortUrl)
        assertEquals(urlShorteningRequest.url, capturedUrlMapping?.fullUrl)
    }

    @Test
    fun shortenUrlForExistingShortUrlIsSuccessful() {
        val originalUrl = "https://google.com"
        val urlShorteningRequest = UrlShorteningRequest(originalUrl)
        val expectedShortUrl = "c5cd9fc1"
        val urlMapping = UrlMapping(expectedShortUrl, originalUrl)

        whenever(urlMappingRepository.findByShortUrl(any())).thenReturn(urlMapping)

        val urlShorteningResponse = urlService.shortenUrl(urlShorteningRequest)

        assertNotNull(urlShorteningResponse)
        assertNotNull(urlShorteningResponse.shortenedUrl)
        assertEquals(expectedShortUrl, urlShorteningResponse.shortenedUrl)
        Mockito.verify(urlMappingRepository, times(1)).findByShortUrl(expectedShortUrl)
        Mockito.verify(urlMappingRepository, times(0)).save(any())
    }

    @Test
    fun shortenUrlForInvalidUrlIsNotSuccessful() {
        val originalUrl = "invalidUrl"
        val urlShorteningRequest = UrlShorteningRequest(originalUrl)

        val exception = assertThrows<UrlInvalidException> { urlService.shortenUrl(urlShorteningRequest) }
        assertEquals("Invalid URL", exception.message)
    }

    @Test
    fun resolveUrlIsSuccessful() {
        val shortUrl = "abc123"
        val urlMapping = UrlMapping(shortUrl, "longTestUrl")

        whenever(urlMappingRepository.findByShortUrl(shortUrl)).thenReturn(urlMapping)

        val originalUrlResponse: OriginalUrlResponse = urlService.resolveUrl(shortUrl)

        assertEquals(urlMapping.fullUrl, originalUrlResponse.originalUrl)
    }

    @Test
    fun resolveUrl_Url_Not_Found() {
        val shortUrl = "abc123"

        whenever(urlMappingRepository.findByShortUrl(shortUrl)).thenReturn(null)

        val exception: UrlNotFoundException = assertThrows<UrlNotFoundException> { urlService.resolveUrl(shortUrl) }
        assertEquals("url not found", exception.message)
    }

    @Test
    fun resolveUrl_Url_Expired_Not_Found_Exception() {
        val shortUrl = "abc123"
        val urlMapping = UrlMapping(shortUrl, "longTestUrl")
        urlMapping.expiresAt = LocalDateTime.now().minusDays(30L)

        whenever(urlMappingRepository.findByShortUrl(shortUrl)).thenReturn(urlMapping)

        val exception: UrlNotFoundException = assertThrows<UrlNotFoundException> { urlService.resolveUrl(shortUrl) }
        assertEquals("url expired", exception.message)
    }
}