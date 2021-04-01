package br.com.poc

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel

@Factory
class GrpcClients {
    fun personClient(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PersonGrpcKt.PersonCoroutineStub {
        return PersonGrpcKt.PersonCoroutineStub(
            channel
        )
    }
}