package br.com.poc.adapters.endpoint

import assertk.assertThat
import assertk.assertions.isIn
import br.com.poc.PersonReply
import br.com.poc.PersonRequest
import br.com.poc.application.services.PersonService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class PersonEndpointTest {
    @Test
    internal fun `should create new person`() {
        val service = mockk<PersonService>()

        val reply = PersonReply.newBuilder().setMessage("Person created!").build()
        val request = PersonRequest.newBuilder()
            .setCpf(123456L)
            .setName("Alison").build()

        every {
            service.savePerson(any(), any())
        } returns Unit

        runBlocking {
            val response = PersonEndpoint(service).createPerson(request)
            assertThat(response).isIn(reply)
        }
    }
}