package org.abondar.experimental.urlshortner.config

import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
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


fun Application.configureRouting() {

    val urlShortenerService by closestDI().instance<UrlShortenerService>()

    routing {


        post("/shorten", {
            tags = listOf("Shortner")
            description = "Generates a short value for the provided url"
            request {
                body<ShortenRequest>()
            }

            response {
                HttpStatusCode.OK to {
                    description = "URL was shortened"
                    body<ShortenResponse>()
                }

                HttpStatusCode.BadRequest to {
                    description = "Provided url is blank or empty"
                }
            }

        }) {
            val request = call.receive<ShortenRequest>()
            val longUrl = request.url

            val shortUrl = urlShortenerService.shortenUrl(longUrl)

            call.respond(ShortenResponse(shortUrl))
        }

        get("/{shortUrl}", {
            tags = listOf("Shortner")
            description = "Redirect short url to original long one"
            request {
                body<ShortenRequest>()
            }

            response {
                HttpStatusCode.Found to {
                    description = "Successfully redirected to shorten"
                }

                HttpStatusCode.NotFound to {
                    description = "Provided url is not found"
                }
            }

        }) {
            val shortUrl = call.parameters["shortUrl"] ?: throw UrlRequestException("Url parameter is required")
            val longUrl = urlShortenerService.getLongUrl(shortUrl)

            call.respondRedirect(longUrl)

        }
    }
}
