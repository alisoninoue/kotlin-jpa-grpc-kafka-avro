package br.com.poc.application.services

import br.com.poc.PersonCreated
import br.com.poc.PersonDetails
import br.com.poc.adapters.kafka.producer.PersonProducer
import br.com.poc.adapters.persistence.entities.PersonEntity
import br.com.poc.adapters.persistence.respositories.PersonRepository
import br.com.poc.application.exceptions.InvalidArgument
import br.com.poc.application.exceptions.NotFound
import io.micronaut.tracing.annotation.ContinueSpan
import io.micronaut.tracing.annotation.SpanTag
import io.micronaut.transaction.annotation.ReadOnly
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.header.internals.RecordHeaders
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class PersonService(private val repository: PersonRepository, private val producer: PersonProducer) {

    @Transactional
    @ContinueSpan
    open fun savePerson(@SpanTag("cpf") cpf: Long, name: String) {
        println("cpf: $cpf + name: $name")
        repository.findById(cpf).ifPresent { throw InvalidArgument("Person $cpf already exists!") }
        repository.save(PersonEntity(cpf, name))

        producer.sendPersonCreated(
            "personCreated-${UUID.randomUUID()}",
            PersonCreated.newBuilder()
                .setCpf(cpf)
                .setName(name).build(),
            RecordHeaders(listOf(RecordHeader("correlationId", UUID.randomUUID().toString().toByteArray())))
        )
    }

    @ReadOnly
    @ContinueSpan
    open fun findBy(cpf: Long): PersonDetails {
        val person = repository.findById(cpf).orElseThrow { NotFound("Person $cpf not found!") }
        return PersonDetails.newBuilder()
            .setCpf(person.cpf!!)
            .setName(person.name)
            .build()
    }
}