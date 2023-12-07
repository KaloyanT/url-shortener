package com.kt.urlshortener.util

import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

class UrlShortener {
    companion object {
        fun generateShortUrl(url: String): String {
            return Hashing.murmur3_32_fixed().hashString(url, StandardCharsets.UTF_8).toString()
        }
    }
}