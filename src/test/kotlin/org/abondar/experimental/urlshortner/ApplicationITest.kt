package org.abondar.experimental.urlshortner

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.abondar.experimental.urlshortner.config.configureDI
import org.abondar.experimental.urlshortner.config.configureRouting
import org.abondar.experimental.urlshortner.config.configureSerialization
import org.abondar.experimental.urlshortner.config.configuireFlyway
import org.abondar.experimental.urlshortner.model.ShortenRequest
import org.abondar.experimental.urlshortner.model.ShortenResponse
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.*

@Testcontainers
class ApplicationITest {
    companion object {

        @Container
        val container = MySQLContainer<Nothing>("mysql:8.4.0").apply {
            withDatabaseName("urlDb")
            withUsername("url")
            withPassword("urlPass")
            withExposedPorts(3306)
        }
    }

    private val objectMapper = jacksonObjectMapper()

    @BeforeTest
    fun setup() {
        container.start()
    }

    @AfterTest
    fun tearDown() {
        container.stop()
    }

    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
            configureDI()
            configuireFlyway()
        }

        environment {
            config = MapApplicationConfig(
                "ktor.datasource.url" to container.jdbcUrl,
                "ktor.datasource.username" to container.username,
                "ktor.datasource.password" to container.password,
                "ktor.datasource.driver" to "com.mysql.cj.jdbc.Driver",
            )
        }

        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }
        }

        val longUrl = "https://google.com"

        client.post("/shorten")
        {
            contentType(ContentType.Application.Json)
            setBody(ShortenRequest(longUrl))
        }
            .apply {
                assertEquals(HttpStatusCode.OK, status)

                val responseBody = bodyAsText()
                assertNotNull(responseBody)

                val response = objectMapper.readValue(responseBody, ShortenResponse::class.java)
                assertNotNull(response.url)
            }
    }
}
