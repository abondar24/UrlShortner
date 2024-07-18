package org.abondar.experimental.urlshortner.config

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "swagger-ui"
            forwardRoot = true
        }
        info {
            title = "URL shortener API"
            version = "latest"
            description = "API for creating short urls and redirecting via them to original ones"
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }

    }
}