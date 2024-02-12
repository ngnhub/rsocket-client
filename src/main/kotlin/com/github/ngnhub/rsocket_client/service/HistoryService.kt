package com.github.ngnhub.rsocket_client.service

import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import com.github.ngnhub.rsocket_client.reposiotry.SavedRequestRepository
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class HistoryService(private val repository: SavedRequestRepository) {

    companion object {
        private const val LOG_TAG = "[HISTORY]"
        private val log = LoggerFactory.getLogger(HistoryService::class.java)
    }

    suspend fun save(request: SavedRequestEntity): Mono<SavedRequestEntity> {
        return repository.save(request)
            .doOnNext {
                log.info("{} History saved, {}", LOG_TAG, it)
            }
    }

    suspend fun getAll() = repository
        .findAll(Sort.by(Sort.Order.desc("savedAt")))
        .asFlow()
}