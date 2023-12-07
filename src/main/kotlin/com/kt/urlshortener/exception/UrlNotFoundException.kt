package com.kt.urlshortener.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UrlNotFoundException(override val message: String) : RuntimeException(message) {
}