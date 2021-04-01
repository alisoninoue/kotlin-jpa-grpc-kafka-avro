package br.com.poc.adapters.kafka.consumer

import br.com.poc.PersonCreated
import io.micronaut.configuration.kafka.annotation.*
import io.micronaut.messaging.Acknowledgement
import io.micronaut.messaging.MessageHeaders
import io.micronaut.tracing.annotation.ContinueSpan

@KafkaListener(
    groupId = "person-consumer-group",
    offsetReset = OffsetReset.EARLIEST,
    offsetStrategy = OffsetStrategy.DISABLED
)
open class PersonListener {

    @Topic("person-created")
    @ContinueSpan
    open fun receive(@KafkaKey key: String, event: PersonCreated, headers: MessageHeaders, acknowledgement: Acknowledgement) {
        val s = headers["correlationId"]
        println(s)
        println("Got Person - ${event.getCpf()} + ${event.getName()} by $key")
        acknowledgement.ack()
    }
}