package com.kt.urlshortener.controller

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.kt.urlshortener.controller.model.OriginalUrlResponse
import com.kt.urlshortener.controller.model.UrlShorteningRequest
import com.kt.urlshortener.controller.model.UrlShorteningResponse
import com.kt.urlshortener.exception.UrlNotFoundException
import com.kt.urlshortener.service.UrlService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest
class UrlControllerV1Test(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var urlService: UrlService

    @Test
    fun urlIsShortenedForNonEmptyUrl() {
        val urlShorteningRequest = UrlShorteningRequest("longTestUrl")

        whenever(urlService.shortenUrl(urlShorteningRequest)).thenReturn(UrlShorteningResponse("test"))

        mockMvc.perform(
            post("/url/v1/shorten")
                .content(jsonMapper().writeValueAsString(urlShorteningRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("shortenedUrl").value("test"))
    }

    @Test
    fun urlIsShortenedForBlankUrl_Bad_Request() {
        val urlShorteningRequest = UrlShorteningRequest("")

        mockMvc.perform(
            post("/url/v1/shorten")
                .content(jsonMapper().writeValueAsString(urlShorteningRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest())
    }

    @Test
    fun urlShorteningRequestForEmptyRequest_Bad_Request() {
        mockMvc.perform(
            post("/url/v1/shorten")
                .content("")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest())
    }

    @Test
    fun urlShorteningRequestForRequestWithInvalidProperty_Bad_Request() {
        mockMvc.perform(
            post("/url/v1/shorten")
                .content(""" { "some": "prop" } """)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest())
    }

    @Test
    fun resolveOriginalUrlWithValidShortUrl() {
        val shortUrl = "a412bc"

        whenever(urlService.resolveUrl(shortUrl)).doReturn(OriginalUrlResponse("originalUrl"))

        mockMvc.perform(
            get("/url/v1/resolve/${shortUrl}")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("originalUrl").value("originalUrl"))
    }

    @Test
    fun resolveOriginalUrlWithInvalidShortUrl_Not_Found_Exception() {
        val shortUrl = "a412bc"

        whenever(urlService.resolveUrl(shortUrl)).doThrow(UrlNotFoundException("url not found"))

        mockMvc.perform(
            get("/url/v1/resolve/${shortUrl}")
        )
            .andExpect(status().isNotFound())
    }

}