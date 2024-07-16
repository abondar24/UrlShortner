package org.abondar.experimental.urlshortner.config

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.server.application.*
import org.abondar.experimental.urlshortner.service.UrlShortenerService
import org.jetbrains.exposed.sql.Database
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

fun Application.configureKoin() {
    val urlModule = module {
        single {
            Database.connect(
                url = environment.config.property("ktor.datasource.url").getString(),
                driver = environment.config.property("ktor.datasource.driver").getString(),
                user = environment.config.property("ktor.datasource.username").getString(),
                password = environment.config.property("ktor.datasource.password").getString(),
            )
        }

        single {
            Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build<String, String>()
        }

        single {
            UrlShortenerService(get(), get())
        }

    }


    startKoin {
        modules(
         urlModule
        )
    }

}