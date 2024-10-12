package org.abondar.experimental.urlshortner.dao

import com.github.benmanes.caffeine.cache.Cache
import org.abondar.experimental.urlshortner.exception.UrlNotFoundException

import redis.clients.jedis.Jedis

class UrlMappingDAO(private val redisClient: Jedis, private val cache: Cache<String, String>) {

    fun save(longUrl: String, shortUrl: String) {

        redisClient.set(shortUrl,longUrl)
        cache.put(shortUrl, longUrl)

    }

    fun findLongUrl(shortUrl: String): String {
        cache.getIfPresent(shortUrl)?.let {
            return it
        }

        var longUrl = redisClient.get(shortUrl)

     return longUrl ?: throw UrlNotFoundException("URL for redirect not found")

    }


}