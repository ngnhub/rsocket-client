package com.github.ngnhub.rsocket_client.mapper

import com.github.ngnhub.rsocket_client.model.RSocketClientRequest
import com.github.ngnhub.rsocket_client.model.RSocketInputRequest
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.awaitFormData

@Component
class RSocketClientRequestMapper(private val validator: Validator) {

    suspend fun mapForm(request: ServerRequest): RSocketClientRequest {
        val map = request.awaitFormData().toInputRequest()
            .also { it.validate() }
        return RSocketClientRequest(map.host!!, map.port!!, map.route!!)
    }

    private fun MultiValueMap<String, String>.toInputRequest(): RSocketInputRequest {
        return RSocketInputRequest(
            getFirst("host"),
            getFirst("port")?.toInt(),
            getFirst("route"),
        )
    }

    suspend fun mapJson(request: ServerRequest): RSocketClientRequest {
        val inputRequest = request.awaitBody(RSocketInputRequest::class)
            .also { it.validate() }
        return RSocketClientRequest(inputRequest.host!!, inputRequest.port!!, inputRequest.route!!)
    }

    private fun RSocketInputRequest.validate() {
        val errors = validator.validate(this)
        if (!errors.isNullOrEmpty()) {
            throw ConstraintViolationException(errors)
        }
    }
}
