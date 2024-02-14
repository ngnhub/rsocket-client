package com.github.ngnhub.rsocket_client.error

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Exception handler for errors are thrown within Mono/Flux thread pool.
 * Such errors are not handled by [org.springframework.web.bind.annotation.ControllerAdvice]
 */
@Component
class WebFluxErrorHandler : ErrorWebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        val bufferFactory: DataBufferFactory = response.bufferFactory()
        response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        val print = ex.message ?: "Unknown error"
        val errorMessage = "$print. Type: ${ex::class}"
        val wrap = bufferFactory.wrap(errorMessage.toByteArray())
        return response.writeWith(Mono.just(wrap))
    }
}
