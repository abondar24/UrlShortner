package org.abondar.experimental.config

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureDataSource() {

    Database.connect(
        url = environment.config.property("ktor.datasource.url").getString(),
        driver = environment.config.property("ktor.datasource.driver").getString(),
        user = environment.config.property("ktor.datasource.username").getString(),
        password = environment.config.property("ktor.datasource.password").getString(),
    )

    val flyway = Flyway.configure().dataSource(
        environment.config.property("ktor.datasource.url").getString(),
        environment.config.property("ktor.datasource.username").getString(),
        environment.config.property("ktor.datasource.password").getString()
    ).load()

    flyway.migrate()

}