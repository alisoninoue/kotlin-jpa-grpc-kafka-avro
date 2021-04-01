package br.com.poc.application.services

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.poc.PersonCreated
import br.com.poc.PersonDetails
import br.com.poc.adapters.kafka.producer.PersonProducer
import br.com.poc.adapters.persistence.entities.PersonEntity
import br.com.poc.adapters.persistence.respositories.PersonRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class PersonServiceTest {
    @Test
    internal fun name() {

        val producer = mockk<PersonProducer>()
        val repository = mockk<PersonRepository>()
        val service = PersonService(repository, producer)

        every {
            producer.sendPersonCreated(ofType(String::class), ofType(PersonCreated::class), any())
        } returns Unit

        every {
            repository.findById(any())
        } returns Optional.empty()

        every {
            repository.save(any())
        } returns PersonEntity(123456L, "Alison")

        service.savePerson(12345678901L, "Alison")

        verify(atMost = 1) { repository.findById(any()) }
        verify(atMost = 1) { repository.save(any()) }
        verify(atMost = 1) { producer.sendPersonCreated(ofType(String::class), ofType(PersonCreated::class), any()) }

    }
}
