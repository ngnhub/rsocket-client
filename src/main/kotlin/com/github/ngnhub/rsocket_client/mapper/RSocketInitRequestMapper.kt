package com.github.ngnhub.rsocket_client.mapper

import com.github.ngnhub.rsocket_client.error.Errors
import com.github.ngnhub.rsocket_client.error.ValidationException
import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest

object RSocketInitRequestMapper {

    private val numberPattern: Regex = Regex("\\d+")

    suspend fun mapForm(request: ServerRequest): RSocketInitRequest {
        val map = request.formData().awaitSingle()
        map.validate()
        return map.mapTo()
    }

    private fun MultiValueMap<String, String>.validate() {
        val errors = Errors()
        getFirst("host") ?: run { errors + "'host' can not be empty" }
        val port = getFirst("port")
        port ?: run { errors + "'port' can not be empty" }
        port?.let {
            if (!numberPattern.matches(it)) {
                errors + "'port' must have a numeric format"
            }
        }
        getFirst("route") ?: run { errors + "'path' can not be empty" }
        if (errors.isNotEmpty()) {
            throw ValidationException(errors.toString())
        }
    }

    private fun MultiValueMap<String, String>.mapTo(): RSocketInitRequest {
        return RSocketInitRequest(
            getFirst("host")!!,
            getFirst("port")!!.toInt(),
            getFirst("route")!!,
        )
    }
}
