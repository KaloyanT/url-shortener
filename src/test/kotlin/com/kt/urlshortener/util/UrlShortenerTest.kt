package com.kt.urlshortener.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class UrlShortenerTest {

    @Test
    fun urlShortenerProducesTheSameOutputForTheSameInput() {
        val testUrl = "https://google.com"

        val shortenedUrl = UrlShortener.generateShortUrl(testUrl)
        val secondShortenedUrl = UrlShortener.generateShortUrl(testUrl)

        assertEquals(shortenedUrl, secondShortenedUrl)
    }

    @Test
    fun urlShortenerProducesDifferentOutputForDifferentInput() {
        val testUrl = "https://google.com"
        val secondTestUrl = "https://youtube.com"

        val shortenedUrl = UrlShortener.generateShortUrl(testUrl)
        val secondShortenedUrl = UrlShortener.generateShortUrl(secondTestUrl)

        assertNotEquals(shortenedUrl, secondShortenedUrl)
    }
}