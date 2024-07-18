package org.abondar.experimental.urlshortner.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortenResponse(
    @JsonProperty(value = "short_url")
    val url: String
)
