package com.github.ngnhub.rsocket_client.router

import com.github.ngnhub.rsocket_client.service.HistoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class HistoryRouteFunctionConfig(private val historyService: HistoryService) {

    @Bean
    fun historyRouteFunction() = coRouter {
        GET("/history") {
            handleGet()
        }
    }

    private suspend fun handleGet(): ServerResponse {
        val history = historyService.getAll()
        return ServerResponse.ok().bodyAndAwait(history)
    }
}
