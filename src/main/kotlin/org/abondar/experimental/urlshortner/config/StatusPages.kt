package org.abondar.experimental.urlshortner.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.abondar.experimental.urlshortner.exception.UrlNotFoundException
import org.abondar.experimental.urlshortner.exception.UrlRequestException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<UrlRequestException> { call, cause ->
            call.respondText(text = cause.message ?: "Bad Request", status = HttpStatusCode.BadRequest)
        }

        exception<UrlNotFoundException> { call, cause ->

            call.respondText(text = cause.message ?: "Not found", status = HttpStatusCode.NotFound)
        }

        statusFile(HttpStatusCode.NotFound,HttpStatusCode.InternalServerError,  filePattern = "pages/errors/#.html")


    }


}