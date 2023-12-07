package com.kt.urlshortener.controller.model

import jakarta.validation.constraints.NotBlank

data class UrlShorteningRequest(@field:NotBlank val url: String)