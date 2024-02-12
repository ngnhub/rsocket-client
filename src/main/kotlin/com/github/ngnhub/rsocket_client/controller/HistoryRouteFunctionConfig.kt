package com.github.ngnhub.rsocket_client.controller

import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import com.github.ngnhub.rsocket_client.service.HistoryService
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HistoryRouteFunctionConfig(private val historyService: HistoryService) {

    @Bean
    fun historyRouteFunction() = coRouter {
        POST("/history") {
            it.handleCreate()
        }
        GET("/history") {
            it.handleGet()
        }
    }

    suspend fun ServerRequest.handleCreate(): ServerResponse {
        val request = bodyToMono(SavedRSocketRequest::class.java)
        val entity = historyService.save(request)
        return ServerResponse.ok().body(entity, SavedRequestEntity::class.java).awaitSingle()
    }

    suspend fun ServerRequest.handleGet(): ServerResponse {
        val history = historyService.getAll()
        return ServerResponse.ok().bodyAndAwait(history)
    }
}
