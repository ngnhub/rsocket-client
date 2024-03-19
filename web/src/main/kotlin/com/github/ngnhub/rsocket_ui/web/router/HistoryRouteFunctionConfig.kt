package com.github.ngnhub.rsocket_ui.web.router

import com.github.ngnhub.rsocket_ui.domain.service.HistoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ServerWebInputException

@Configuration
class HistoryRouteFunctionConfig(private val historyService: HistoryService) {

    @Bean
    fun historyRouteFunction() = coRouter {
        GET("/history") {
            handleGet()
        }
        DELETE("/history") {
            it.handleDelete()
        }
        DELETE("/history/{id}") {
            it.handleDelete()
        }
    }

    private suspend fun handleGet(): ServerResponse {
        val history = historyService.getAll()
        return ServerResponse.ok().bodyAndAwait(history)
    }

    private suspend fun ServerRequest.handleDelete(): ServerResponse {
        if (pathVariables().isEmpty()) {
            historyService.deleteAll()
        } else {
            val param = pathVariable("id")
            try {
                historyService.deleteBy(param.toLong())
            } catch (ex: NumberFormatException) {
                throw ServerWebInputException("Invalid value: '$param'. ID must be numeric")
            }
        }
        return ServerResponse.ok().buildAndAwait();
    }
}
