package com.github.ngnhub.rsocket_client.client

import com.github.ngnhub.rsocket_client.error.RSocketListenerError
import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import kotlinx.coroutines.flow.catch
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.stereotype.Service

@Service
class RsocketListener {

    suspend fun request(request: RSocketInitRequest) =
        RSocketRequester.builder()
            .tcp(request.host, request.port)
            .route(request.route)
            .retrieveFlow<String>()
            .catch { throw RSocketListenerError(it.message) }
}
