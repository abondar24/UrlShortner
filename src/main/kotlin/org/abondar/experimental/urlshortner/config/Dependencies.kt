package org.abondar.experimental.urlshortner.config

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.server.application.*
import org.abondar.experimental.urlshortner.dao.UrlMappingDAO
import org.abondar.experimental.urlshortner.service.UrlShortenerService
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import redis.clients.jedis.Jedis
import java.util.concurrent.TimeUnit

fun Application.configureDI() {
    di {
        bind<UrlMappingDAO>() with singleton {

            val redisUrl = environment.config.property("ktor.redis.url").getString()
            val redisDb = environment.config.property("ktor.redis.db").getString()
            val redisPassword = environment.config.property("ktor.redis.password").getString()

//            val redisClient = Jedis(redisUrl).apply {
//                auth(redisPassword)
//            }
//ENABLE PASSWORD IF IT IS SET
            val redisClient = Jedis(redisUrl)

            redisClient.select(redisDb.toInt())

            val cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build<String, String>()

            UrlMappingDAO(redisClient, cache)
        }


        bind<UrlShortenerService>() with singleton {
            UrlShortenerService(instance())
        }

    }


}