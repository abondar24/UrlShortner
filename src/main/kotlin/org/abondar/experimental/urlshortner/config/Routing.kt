package org.abondar.experimental.urlshortner.config

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.abondar.experimental.urlshortner.exception.UrlRequestException
import org.abondar.experimental.urlshortner.model.ShortenRequest
import org.abondar.experimental.urlshortner.model.ShortenResponse
import org.abondar.experimental.urlshortner.service.UrlShortenerService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import org.kodein.di.on


fun Application.configureRouting() {

    val urlShortenerService by closestDI().instance<UrlShortenerService>()

    routing {
        post ("/shorten") {
            val request = call.receive<ShortenRequest>()
            val longUrl = request.url

            val shortUrl = urlShortenerService.shortenUrl(longUrl)

            call.respond(ShortenResponse(shortUrl))
        }

        get("/{shortUrl}") {
            val shortUrl = call.parameters["shortUrl"]?: throw UrlRequestException("Url parameter is required")
            val longUrl = urlShortenerService.getLongUrl(shortUrl)

            call.respondRedirect(longUrl)

        }
    }
}
