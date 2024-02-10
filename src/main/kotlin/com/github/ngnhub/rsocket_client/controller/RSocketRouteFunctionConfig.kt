package com.github.ngnhub.rsocket_client.controller

import com.github.ngnhub.rsocket_client.client.RsocketListener
import com.github.ngnhub.rsocket_client.mapper.RSocketInitRequestMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class RSocketRouteFunctionConfig(private val listener: RsocketListener) {

    @Bean
    fun rSocketClientRouter() = coRouter {
        POST("/route") {
            handleRequest(it)
        }
    }

    private suspend fun CoRouterFunctionDsl.handleRequest(request: ServerRequest): ServerResponse {
        val rSocketInitRequest = RSocketInitRequestMapper.map(request)
        val flow = listener.request(rSocketInitRequest)
        return ok().contentType(MediaType.TEXT_EVENT_STREAM).bodyAndAwait(flow)
    }
}
