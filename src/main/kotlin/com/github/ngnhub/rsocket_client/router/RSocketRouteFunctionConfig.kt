package com.github.ngnhub.rsocket_client.router

import com.github.ngnhub.rsocket_client.client.RsocketListener
import com.github.ngnhub.rsocket_client.mapper.RSocketClientRequestMapper
import com.github.ngnhub.rsocket_client.model.RSocketClientRequest
import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import com.github.ngnhub.rsocket_client.service.HistoryService
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
    private val mapper: RSocketClientRequestMapper
) {

    @Bean
    fun rSocketClientRouter() = coRouter {
        POST("/route") {
            handleRequest(it)
        }
    }

    private suspend fun CoRouterFunctionDsl.handleRequest(request: ServerRequest): ServerResponse {
        val clientRequest = request.toRsocketClientRequest()
        val entity = clientRequest.toEntity()
        asyncTaskCoroutineScope.launch {
            historyService.save(entity)
        }
        val flow = listener.request(clientRequest)
        return ok().contentType(TEXT_EVENT_STREAM).bodyAndAwait(flow)
    }

    suspend fun ServerRequest.toRsocketClientRequest(): RSocketClientRequest {
        val contentType: MediaType = headers()
            .contentType()
            .orElseThrow { ServerWebInputException("Content type is empty") }
        if (contentType.isCompatibleWith(APPLICATION_JSON)) {
            return mapper.mapJson(this)
        }
        if (contentType.isCompatibleWith(APPLICATION_FORM_URLENCODED)) {
            return mapper.mapForm(this)
        }
        throw ServerWebInputException(
            "Unsupported Content type: ${contentType}. Must be $APPLICATION_JSON or $APPLICATION_FORM_URLENCODED"
        )
    }

    fun RSocketClientRequest.toEntity() = SavedRequestEntity(null, host, port, route)
}
