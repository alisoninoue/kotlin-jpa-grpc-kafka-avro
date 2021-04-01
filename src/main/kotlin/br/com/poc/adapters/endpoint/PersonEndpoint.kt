package br.com.poc.adapters.endpoint

import br.com.poc.*
import br.com.poc.application.services.PersonService
import io.micronaut.grpc.annotation.GrpcService
import io.micronaut.tracing.annotation.NewSpan
import io.micronaut.tracing.annotation.SpanTag
import io.opentracing.Tracer
import org.slf4j.MDC
import javax.inject.Inject

@GrpcService
open class PersonEndpoint(
    private val service: PersonService
) : PersonGrpcKt.PersonCoroutineImplBase() {

    companion object : EndpointCompanion()

    @Inject
    lateinit var tracer: Tracer

    @NewSpan("create-person-grpc")
    override suspend fun createPerson(@SpanTag("request") request: PersonRequest): PersonReply = runCatching {
        MDC.put("request", request.toString())
        service.savePerson(request.cpf, request.name)
        MDC.clear()

        PersonReply.newBuilder().setMessage("Person created!").build()
    }.onFailure { throw it.toStatusException() }.getOrThrow()

    @NewSpan("findById-person-grpc")
    override suspend fun findById(request: PersonIdRequest): PersonDetails = runCatching {
        tracer.activeSpan().setTag("personId", request.cpf)
        service.findBy(request.cpf)
    }.onFailure { throw it.toStatusException() }.getOrThrow()
}