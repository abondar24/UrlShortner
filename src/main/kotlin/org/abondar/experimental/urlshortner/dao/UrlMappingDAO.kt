package org.abondar.experimental.urlshortner.dao

import com.github.benmanes.caffeine.cache.Cache
import org.abondar.experimental.urlshortner.exception.UrlRequestException
import org.abondar.experimental.urlshortner.model.UrlMapping
import org.abondar.experimental.urlshortner.model.UrlMapping.longUrl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class UrlMappingDAO(private val db: Database, private val cache: Cache<String, String>) {

    fun save(longUrl: String, shortUrl: String) {

        transaction(db) {
            addLogger(StdOutSqlLogger)

            UrlMapping.insert {
                it[UrlMapping.longUrl] = longUrl
                it[UrlMapping.shortUrl] = shortUrl
                it[createdAt] = LocalDateTime.now()
            }
        }

        cache.put(shortUrl, longUrl)

    }

    fun findLongUrl(shortUrl: String): String {
        cache.getIfPresent(shortUrl)?.let {
            return it
        }

     return transaction(db) {
            addLogger(StdOutSqlLogger)
            UrlMapping.select(longUrl)
                .where(UrlMapping.shortUrl.eq(shortUrl))
                .firstOrNull()?.let {
                    val longUrl = it[UrlMapping.longUrl]
                    longUrl
                } ?: throw UrlRequestException("URL for redirect not found")

        }

    }


}