package br.com.poc.adapters.kafka.producer

import br.com.poc.PersonCreated
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.tracing.annotation.NewSpan
import org.apache.kafka.common.header.Headers

@KafkaClient
interface PersonProducer {

    @Topic("person-created")
    fun sendPersonCreated(@KafkaKey key: String, person: PersonCreated, headers: Headers)
}