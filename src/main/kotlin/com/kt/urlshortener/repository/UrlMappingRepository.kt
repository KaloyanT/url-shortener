package com.kt.urlshortener.repository

import com.kt.urlshortener.db.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlMappingRepository : JpaRepository<UrlMapping, Long> {
    fun findByShortUrl(shortUrl: String): UrlMapping?
}