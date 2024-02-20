package com.github.ngnhub.rsocket_client.router

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ngnhub.rsocket_client.model.SavedRequest
import com.github.ngnhub.rsocket_client.service.HistoryService
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class HistoryRouteFunctionConfigTest {

    @Mock
    private lateinit var historyService: HistoryService
    private lateinit var routeFunctionConfig: HistoryRouteFunctionConfig
    private lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() {
        routeFunctionConfig = HistoryRouteFunctionConfig(historyService)
        client = WebTestClient
            .bindToRouterFunction(routeFunctionConfig.historyRouteFunction())
            .build()
    }

    @Test
    fun `should get all history`() {
        runBlocking {
            // given
            val date = LocalDateTime.of(2025, 1, 1, 1, 1)
            val request = SavedRequest(1, "host", 1111, "route", date)
            val historyList = listOf(request)
            whenever(historyService.getAll()).thenReturn(historyList.asFlow())
            val json = ObjectMapper().findAndRegisterModules().writeValueAsString(historyList)

            // then
            runBlocking {
                client.get()
                    .uri("/history")
                    .exchange()
                    .expectStatus()
                    .isOk
                    .expectBody()
                    .json(json)
            }
        }
    }
}
