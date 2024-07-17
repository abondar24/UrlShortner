package org.abondar.experimental.urlshortner.service

import org.abondar.experimental.urlshortner.exception.UrlRequestException
import org.abondar.experimental.urlshortner.dao.UrlMappingDAO
import org.slf4j.LoggerFactory

import java.util.*

class UrlShortenerService(private val dao: UrlMappingDAO)  {

    private val logger = LoggerFactory.getLogger(UrlShortenerService::class.java)


    fun shortenUrl(longUrl: String): String {

        if (longUrl.isBlank()) {
            throw UrlRequestException("URL cannot be blank")
        }

        val shortUrl = UUID.randomUUID().toString().substring(0, 6)
        dao.save(longUrl, shortUrl)

        logger.info("Shortened URL: $longUrl to $shortUrl")

        return shortUrl
    }

    fun getLongUrl(shortUrl: String): String {
        return dao.findLongUrl(shortUrl)
    }

}