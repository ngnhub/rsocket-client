package com.github.ngnhub.rsocket_client.controller

import com.github.ngnhub.rsocket_client.error.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import java.net.ConnectException


@ControllerAdvice
class ExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    companion object {
        const val LOG_TAG = "[HANDLED EXCEPTION]"
        private val log = LoggerFactory.getLogger(ExceptionHandlerAdvice::class.java)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(exc: ValidationException): ResponseEntity<String> {
        log.error(LOG_TAG, exc)
        return ResponseEntity.badRequest()
            .body(exc.message)
    }

    @ExceptionHandler(ConnectException::class)
    fun handleRSocketListenerError(exc: ConnectException): ResponseEntity<String> {
        log.error(LOG_TAG, exc)
        return ResponseEntity.badRequest()
            .body(exc.message)
    }
}