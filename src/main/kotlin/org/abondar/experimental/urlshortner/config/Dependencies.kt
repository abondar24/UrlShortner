package org.abondar.experimental.urlshortner.config

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.server.application.*
import org.abondar.experimental.urlshortner.dao.UrlMappingDAO
import org.abondar.experimental.urlshortner.service.UrlShortenerService
import org.jetbrains.exposed.sql.Database
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.CallScope
import org.kodein.di.ktor.di
import org.kodein.di.scoped
import org.kodein.di.singleton
import java.util.concurrent.TimeUnit

fun Application.configureDI() {
    di {
        bind<UrlMappingDAO>() with singleton {
            val db = Database.connect(
                url = environment.config.property("ktor.datasource.url").getString(),
                driver = environment.config.property("ktor.datasource.driver").getString(),
                user = environment.config.property("ktor.datasource.username").getString(),
                password = environment.config.property("ktor.datasource.password").getString(),
            )

            val cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build<String, String>()

            UrlMappingDAO(db, cache)
        }


        bind<UrlShortenerService>() with singleton {
            UrlShortenerService(instance())
        }

    }


}