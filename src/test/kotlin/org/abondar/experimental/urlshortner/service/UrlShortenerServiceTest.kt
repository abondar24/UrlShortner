package org.abondar.experimental.urlshortner.service

import com.github.benmanes.caffeine.cache.Cache
import io.mockk.*
import org.abondar.experimental.urlshortner.dao.UrlMappingDAO
import org.abondar.experimental.urlshortner.exception.UrlNotFoundException
import org.abondar.experimental.urlshortner.exception.UrlRequestException
import org.jetbrains.exposed.sql.Database
import org.junit.Assert.*
import org.kodein.di.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull


class UrlShortenerServiceTest {

    private lateinit var database: Database
    private lateinit var cache: Cache<String, String>
    private lateinit var urlMappingDAO: UrlMappingDAO
    private lateinit var kodein: DI
    private lateinit var urlShortenerService: UrlShortenerService


    @BeforeTest
    fun setup() {
        database = mockk(relaxed = true)
        cache = mockk(relaxed = true)
        urlMappingDAO = mockk(relaxed = true)

        kodein = DI {
            bind<Database>() with singleton { database }
            bind<Cache<String, String>>() with singleton { cache }
            bind<UrlMappingDAO>() with singleton { urlMappingDAO }
            bind<UrlShortenerService>() with singleton { UrlShortenerService(instance()) }
        }

        urlShortenerService = kodein.direct.instance()
    }


    @AfterTest
    fun tearDown() {
        clearMocks(database, cache, urlMappingDAO)
    }

    @Test
    fun `test url shortener `() {
        val longUrl = "http://www.test.com"

        every { urlMappingDAO.save(any(), any()) } returns Unit


        val result = urlShortenerService.shortenUrl(longUrl)

        assertNotNull(result)
        assertFalse(result.isEmpty())

        verify { urlMappingDAO.save(any(), any()) }


    }

    @Test
    fun `test url shortener empty url`() {
        val longUrl = ""

        val exception = assertThrows(UrlRequestException::class.java) {
            urlShortenerService.shortenUrl(longUrl)
        }

        assertEquals("URL cannot be blank", exception.message)

    }

    @Test
    fun `test find long url `() {
        val longUrl = "http://www.test.com"
        val shortUrl = "sdd"

        every { urlMappingDAO.findLongUrl(shortUrl) } returns longUrl
        val result = urlShortenerService.getLongUrl(shortUrl)

        assertEquals(longUrl, result)

    }


    @Test
    fun `test long url not found `() {
        val shortUrl = "sdd"

        every { urlMappingDAO.findLongUrl(shortUrl) } throws UrlNotFoundException("URL for redirect not found")

        val exception = assertThrows(UrlNotFoundException::class.java) {
            urlShortenerService.getLongUrl(shortUrl)
        }

        assertEquals("URL for redirect not found", exception.message)

    }

}