package com.github.ngnhub.rsocket_ui.web.service

import com.github.ngnhub.rsocket_ui.web.model.RSocketClientRequest
import com.github.ngnhub.rsocket_ui.web.model.SavedRequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class RouteService(
    private val listener: com.github.ngnhub.rsocket_ui.web.client.RsocketListener,
    private val historyService: HistoryService,
    private val asyncTaskCoroutineScope: CoroutineScope,
    private val clock: Clock
) {

    companion object {
        private const val LOG_TAG = "[RSOCKET_ROUTE]"

        private val log = LoggerFactory.getLogger(RouteService::class.java)
    }

    suspend fun route(clientRequest: RSocketClientRequest): Flow<String> {
        val entity = clientRequest.toEntity()
        asyncTaskCoroutineScope.launch {
            historyService.save(entity)
        }
        return listener.request(clientRequest)
            .also { with(clientRequest) { log.info("{} Listen to {}", LOG_TAG, "$host:$port/$route") } }
    }

    private fun RSocketClientRequest.toEntity() =
        SavedRequestEntity(null, host, port, route ?: "", LocalDateTime.now(clock))
}
