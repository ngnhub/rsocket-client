package com.github.ngnhub.rsocket_client.service

import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import com.github.ngnhub.rsocket_client.reposiotry.SavedRequestRepository
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class HistoryService(private val repository: SavedRequestRepository) {

    companion object {
        private const val LOG_TAG = "[HISTORY]"
        private val log = LoggerFactory.getLogger(HistoryService::class.java)
    }

    suspend fun save(request: Mono<SavedRSocketRequest>): Mono<SavedRequestEntity> {
        return request.map {
            SavedRequestEntity(
                null,
                it.rSocketInitRequest.host,
                it.rSocketInitRequest.port,
                it.rSocketInitRequest.route
            )
        }.flatMap { repository.save(it) }
            .doOnNext {
                log.info("{} History saved, {}", LOG_TAG, it)
            }
    }

    suspend fun getAll() = repository.findAll().asFlow()
}