package org.abondar.experimental.urlshortner

import io.ktor.server.application.*
import org.abondar.experimental.urlshortner.config.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSwagger()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureFlyway()
    configureDI()
    configureStatusPages()
}

