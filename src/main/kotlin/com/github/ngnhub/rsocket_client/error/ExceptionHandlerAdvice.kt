package com.github.ngnhub.rsocket_client.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono


@ControllerAdvice
class ExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    companion object {
        const val LOG_TAG = "[HANDLED EXCEPTION]"
        private val log = LoggerFactory.getLogger(ExceptionHandlerAdvice::class.java)
    }

    @ExceptionHandler(Throwable::class)
    fun handleRSocketListenerError(exc: Throwable): ResponseEntity<String> {
        log.error(LOG_TAG, exc)
        return ResponseEntity.internalServerError()
            .body(exc.message)
    }

    override fun handleServerWebInputException(
        ex: ServerWebInputException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        log.error(LOG_TAG, ex.cause)
        val body: ResponseEntity<Any> = ResponseEntity.internalServerError()
            .body(ex.cause?.message)
        return Mono.just(body)
    }
}
