package org.abondar.experimental

import io.ktor.server.application.*
import org.abondar.experimental.config.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSwagger()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
