package com.github.ngnhub.rsocket_client.client

import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import kotlinx.coroutines.reactive.asFlow
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlux
import org.springframework.stereotype.Service

@Service
class RsocketListener {

    suspend fun request(request: RSocketInitRequest) =
        RSocketRequester.builder()
            .tcp(request.host, request.port)
            .route(request.route)
            .retrieveFlux<String>()
            .asFlow()
}

// TODO: error handling  https://www.baeldung.com/spring-webflux-errors
