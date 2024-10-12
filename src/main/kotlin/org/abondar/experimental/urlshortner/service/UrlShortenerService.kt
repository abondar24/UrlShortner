package org.abondar.experimental.urlshortner.service

import org.abondar.experimental.urlshortner.exception.UrlRequestException
import org.abondar.experimental.urlshortner.dao.UrlMappingDAO
import org.slf4j.LoggerFactory

import java.util.*
import kotlin.math.abs

class UrlShortenerService(private val dao: UrlMappingDAO)  {

    private val logger = LoggerFactory.getLogger(UrlShortenerService::class.java)


    fun shortenUrl(longUrl: String): String {

        if (longUrl.isBlank()) {
            throw UrlRequestException("URL cannot be blank")
        }

        val shortUrl = encodeUrl()
        dao.save(longUrl, shortUrl)

        logger.info("Shortened URL: $longUrl to $shortUrl")

        return shortUrl
    }

    private fun encodeUrl(): String {
       var id = UUID.randomUUID().mostSignificantBits
       val shortUrl = StringBuilder()

        if (id == 0L){
            id = UUID.randomUUID().leastSignificantBits
        }

        id = abs(id)

        while (id >0){
            shortUrl.append(BASE62[((id % 62).toInt())])
            id /=62
        }

        //result is built from least significant digit - need reverse
        return shortUrl.reversed().toString()
    }

    fun getLongUrl(shortUrl: String): String {
        return dao.findLongUrl(shortUrl)
    }

    companion object {
        private const val BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    }
}