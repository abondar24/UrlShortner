package org.abondar.experimental.urlshortner.config

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.flywayConfig() {

    val flyway = Flyway.configure().dataSource(
        environment.config.property("ktor.datasource.url").getString(),
        environment.config.property("ktor.datasource.username").getString(),
        environment.config.property("ktor.datasource.password").getString()
    ).load()

    flyway.migrate()

}