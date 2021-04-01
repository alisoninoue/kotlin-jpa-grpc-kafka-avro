package br.com.poc.adapters.endpoint

import br.com.poc.application.exceptions.InvalidArgument
import br.com.poc.application.exceptions.NotFound
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.StatusException
import io.grpc.protobuf.StatusProto

open class EndpointCompanion {

    fun Throwable.toStatusException(): Throwable =
        when (this) {
            is NotFound ->
                StatusProto.toStatusException(
                    Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).setMessage(this.message).build()
                )
            is InvalidArgument ->
                StatusProto.toStatusException(
                    Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(this.message).build()
                )
            is StatusException -> this
            else ->
                StatusProto.toStatusException(
                    Status.newBuilder().setCode(Code.INTERNAL_VALUE).setMessage(this.message + this.cause).build()
                ).also {
                    println(this.message)
                    println(this.cause)
                    println(this.stackTraceToString())
                }
        }
}