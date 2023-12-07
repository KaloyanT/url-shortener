package com.kt.urlshortener.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "url_mapping",
    indexes = [
        Index(name = "idx_short_url", columnList = "shortUrl", unique = true)
    ])
open class UrlMapping(
    @Column(unique = true, nullable = false)
    open var shortUrl: String,

    @Column(nullable = false)
    open var fullUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0

    @Column(nullable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    open var expiresAt: LocalDateTime = LocalDateTime.now().plusDays(30)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UrlMapping

        if (id != other.id) return false
        if (shortUrl != other.shortUrl) return false
        if (fullUrl != other.fullUrl) return false
        if (createdAt != other.createdAt) return false
        if (expiresAt != other.expiresAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shortUrl.hashCode()
        result = 31 * result + fullUrl.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + expiresAt.hashCode()
        return result
    }


}

