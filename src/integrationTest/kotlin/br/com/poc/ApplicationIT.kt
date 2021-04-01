package br.com.poc

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.inject.Inject


@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class ApplicationIT {
    companion object {
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
            withDatabaseName("postgres")
            withUsername("postgres")
            withPassword("pwd123")
            start()
            System.setProperty("datasources.default.url", this.jdbcUrl)
            System.setProperty("datasources.default.username", this.username)
            System.setProperty("datasources.default.password", this.password)
            System.setProperty("datasources.default.driverClassName", this.driverClassName)
        }
    }

    @Inject
    lateinit var personClient: PersonGrpcKt.PersonCoroutineStub

    @Test
    fun `should call endpoint`() = runBlocking {
        Assertions.assertEquals(
            "Person created!",
            personClient.createPerson(
                PersonRequest.newBuilder()
                    .setCpf(12345678901L)
                    .setName("Alison")
                    .build()
            ).message
        )
    }
}