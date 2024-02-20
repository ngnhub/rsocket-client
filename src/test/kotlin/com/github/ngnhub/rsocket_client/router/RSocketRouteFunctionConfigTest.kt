package com.github.ngnhub.rsocket_client.router

import com.github.ngnhub.rsocket_client.client.RsocketListener
import com.github.ngnhub.rsocket_client.mapper.RSocketClientRequestMapper
import com.github.ngnhub.rsocket_client.model.RSocketClientRequest
import com.github.ngnhub.rsocket_client.service.HistoryService
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import reactor.core.publisher.Flux
import reactor.test.StepVerifier


@ExtendWith(MockitoExtension::class)
class RSocketRouteFunctionConfigTest {

    @Mock
    private lateinit var listener: RsocketListener

    @Mock
    private lateinit var historyService: HistoryService

    private val asyncTaskCoroutineScope = TestScope()

    @Mock
    private lateinit var mapper: RSocketClientRequestMapper

    private lateinit var client: WebTestClient

    private lateinit var config: RSocketRouteFunctionConfig

    @BeforeEach
    fun setUp() {
        config = RSocketRouteFunctionConfig(listener, historyService, asyncTaskCoroutineScope, mapper)
        client = WebTestClient
            .bindToRouterFunction(config.rSocketClientRouter())
            .build()
    }

    @Test
    fun `should call RSocket listener by form data`() {
        runBlocking {
            // given
            val formData = LinkedMultiValueMap<String, String>()
            val request = RSocketClientRequest("host", "1111".toInt(), "route")

            whenever(mapper.mapForm(any())).thenReturn(request)
            whenever(listener.request(request)).thenReturn(Flux.just("rsocket_value").asFlow())

            // when
            val fluxExchangeResult = client.post()
                .uri("/route")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType("text/event-stream;charset=UTF-8")
                .returnResult(String::class.java)

            // then
            StepVerifier.create(fluxExchangeResult.responseBody)
                .expectNext("rsocket_value")
                .verifyComplete()
        }
    }

    @Test
    fun `should call RSocket listener by json`() {
        runBlocking {
            // given
            val request = RSocketClientRequest("host", "1111".toInt(), "route")

            whenever(mapper.mapJson(any())).thenReturn(request)
            whenever(listener.request(request)).thenReturn(Flux.just("rsocket_value").asFlow())

            // when
            val fluxExchangeResult = client.post()
                .uri("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType("text/event-stream;charset=UTF-8")
                .returnResult(String::class.java)

            // then
            StepVerifier.create(fluxExchangeResult.responseBody)
                .expectNext("rsocket_value")
                .verifyComplete()
        }
    }

    @Test
    fun `should throw unsupported content type empty`() {
        runBlocking {
            client.post()
                .uri("/route")
                .bodyValue("{}")
                .exchange()
                .expectStatus()
                .isBadRequest()
        }
    }

    @Test
    fun `should throw unsupported content type invalid`() {
        runBlocking {
            client.post()
                .uri("/route")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("{}")
                .exchange()
                .expectStatus()
                .isBadRequest()
        }
    }
}
