package com.github.ngnhub.rsocket_client.router

import com.github.ngnhub.rsocket_client.client.RsocketListener
import com.github.ngnhub.rsocket_client.mapper.RSocketInitRequestMapper
import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import com.github.ngnhub.rsocket_client.service.HistoryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class RSocketRouteFunctionConfig(
    private val listener: RsocketListener,
    private val historyService: HistoryService,
    private val asyncTaskCoroutineScope: CoroutineScope
) {

    @Bean
    fun rSocketClientRouter() = coRouter {
        POST("/route") {
            handleRequest(it)
        }
    }

    private suspend fun CoRouterFunctionDsl.handleRequest(request: ServerRequest): ServerResponse {
        val rSocketInitRequest = RSocketInitRequestMapper.map(request)
        val entity = rSocketInitRequest.toEntity()
        asyncTaskCoroutineScope.launch {
            historyService.save(entity)
        }
        val flow = listener.request(rSocketInitRequest)
        return ok().contentType(MediaType.TEXT_EVENT_STREAM).bodyAndAwait(flow)
    }

    fun RSocketInitRequest.toEntity() = SavedRequestEntity(null, host, port, route)
}
