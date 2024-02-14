package com.github.ngnhub.rsocket_client.router

import com.github.ngnhub.rsocket_client.client.RsocketListener
import com.github.ngnhub.rsocket_client.error.UnsupportedContentType
import com.github.ngnhub.rsocket_client.error.handle
import com.github.ngnhub.rsocket_client.mapper.RSocketInitRequestMapper
import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import com.github.ngnhub.rsocket_client.service.HistoryService
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ServerWebInputException

@Configuration
class RSocketRouteFunctionConfig(
    private val listener: RsocketListener,
    private val historyService: HistoryService,
    private val asyncTaskCoroutineScope: CoroutineScope,
    private val validator: Validator
) {

    @Bean
    fun rSocketClientRouter() = coRouter {
        POST("/route") {
            handleRequest(it)
        }
    }

    private suspend fun CoRouterFunctionDsl.handleRequest(request: ServerRequest): ServerResponse {
        val rSocketInitRequest = request.toRsocketInitRequest()
        val entity = rSocketInitRequest.toEntity()
        asyncTaskCoroutineScope.launch {
            historyService.save(entity)
        }
        val flow = listener.request(rSocketInitRequest)
        return ok().contentType(TEXT_EVENT_STREAM).bodyAndAwait(flow)
    }

    suspend fun ServerRequest.toRsocketInitRequest(): RSocketInitRequest {
        val contentType: MediaType = headers()
            .contentType()
            .orElseThrow { UnsupportedContentType("Content type is empty") }
        if (contentType == APPLICATION_JSON) {
            try {
                val awaitBody = awaitBody(RSocketInitRequest::class)
                val errors = validator.validate(awaitBody)
                if (errors.isNotEmpty()) {
                    throw ConstraintViolationException(errors)
                }
                return awaitBody
            } catch (err: ServerWebInputException) {
                handle(err)
            }
        }
        if (contentType == APPLICATION_FORM_URLENCODED) {
            return RSocketInitRequestMapper.mapForm(this)
        }
        throw UnsupportedContentType(
            "Unsupported Content type: ${contentType.type}. Must be $APPLICATION_JSON or $APPLICATION_FORM_URLENCODED"
        )
    }

    fun RSocketInitRequest.toEntity() = SavedRequestEntity(null, host!!, port!!, route!!)
}
