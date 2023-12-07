package com.kt.urlshortener.service

import com.kt.urlshortener.controller.model.OriginalUrlResponse
import com.kt.urlshortener.controller.model.UrlShorteningRequest
import com.kt.urlshortener.controller.model.UrlShorteningResponse
import com.kt.urlshortener.db.UrlMapping
import com.kt.urlshortener.exception.UrlNotFoundException
import com.kt.urlshortener.repository.UrlMappingRepository
import com.kt.urlshortener.util.UrlShortener
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UrlService(val urlMappingRepository: UrlMappingRepository) {

    fun shortenUrl(urlShorteningRequest: UrlShorteningRequest): UrlShorteningResponse {
        val shortUrl: String = UrlShortener.generateShortUrl(urlShorteningRequest.url)
        val existingUrlMapping: UrlMapping? = urlMappingRepository.findByShortUrl(shortUrl)

        if (existingUrlMapping == null) {
            urlMappingRepository.save(UrlMapping(shortUrl, urlShorteningRequest.url))
        }

        return UrlShorteningResponse(shortUrl)
    }

    fun resolveUrl(shortUrl: String): OriginalUrlResponse {
        val urlMapping: UrlMapping = urlMappingRepository.findByShortUrl(shortUrl)
            ?: throw UrlNotFoundException("url not found")

        if (LocalDateTime.now().isAfter(urlMapping.expiresAt)) {
            urlMappingRepository.delete(urlMapping)
            throw UrlNotFoundException("url expired")
        }

        return OriginalUrlResponse(urlMapping.fullUrl)
    }
}