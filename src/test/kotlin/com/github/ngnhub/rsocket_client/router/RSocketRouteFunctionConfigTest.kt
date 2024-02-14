package com.github.ngnhub.rsocket_client.router

import com.github.ngnhub.rsocket_client.client.RsocketListener
import com.github.ngnhub.rsocket_client.model.RSocketClientRequest
import kotlinx.coroutines.reactive.asFlow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import reactor.core.publisher.Flux
import reactor.test.StepVerifier


@ExtendWith(MockitoExtension::class)
class RSocketRouteFunctionConfigTest {

    @Mock
    lateinit var listener: RsocketListener

    private lateinit var client: WebTestClient
    private lateinit var config: RSocketRouteFunctionConfig

    @BeforeEach
    fun setUp() {
        config = RSocketRouteFunctionConfig(listener)
        client = WebTestClient
            .bindToRouterFunction(config.rSocketClientRouter())
            .build()
    }

    @Test
    fun rSocketClientRouter() {
        // given
        val formData = LinkedMultiValueMap<String, String>()
        val host = "localhost"
        val port = "1111"
        val route = "/some/route"
        formData.add("host", host)
        formData.add("port", port)
        formData.add("route", route)
        val request = RSocketClientRequest(host, port.toInt(), route)
        `when`(listener.request(request)).thenReturn(Flux.just("rsocket_value").asFlow())

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

        StepVerifier.create(fluxExchangeResult.responseBody)
            .expectNext("rsocket_value")
            .verifyComplete()
    }
}
