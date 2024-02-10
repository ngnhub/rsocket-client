package com.github.ngnhub.rsocket_client.controller

import com.github.ngnhub.rsocket_client.error.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ExceptionHandlerAdvice : ResponseEntityExceptionHandler() {

    companion object {
        const val LOG_TAG = "[HANDLED EXCEPTION]"
        private val log = LoggerFactory.getLogger(ExceptionHandlerAdvice::class.java)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(exc: ValidationException) {
        log.error(LOG_TAG, exc)
        ResponseEntity.badRequest()
            .contentType(MediaType.TEXT_PLAIN)
            .body(exc.message)
    }
}
