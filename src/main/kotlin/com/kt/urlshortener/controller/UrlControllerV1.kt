package com.kt.urlshortener.controller

import com.kt.urlshortener.controller.model.OriginalUrlResponse
import com.kt.urlshortener.controller.model.UrlShorteningRequest
import com.kt.urlshortener.controller.model.UrlShorteningResponse
import com.kt.urlshortener.service.UrlService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/url/v1")
class UrlControllerV1(val urlService: UrlService) {

    @PostMapping("/shorten")
    fun shortenUrl(@Valid @RequestBody urlShorteningRequest: UrlShorteningRequest): UrlShorteningResponse {
        return urlService.shortenUrl(urlShorteningRequest)
    }

    @GetMapping("/resolve/{shortUrl}")
    fun getOriginalUrl(@PathVariable("shortUrl") shortUrl: String): OriginalUrlResponse {
        return urlService.resolveUrl(shortUrl)
    }

}