package com.github.ngnhub.rsocket_client.service

import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
import com.github.ngnhub.rsocket_client.reposiotry.CSVRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HistoryService(private val repository: CSVRepository) {
    companion object {
        private const val LOG_TAG = "[HISTORY]"
        private val log = LoggerFactory.getLogger(HistoryService::class.java)
    }

    suspend fun save(request: SavedRSocketRequest) =
        repository.save(request).also { log.info("{} History saved, {}", LOG_TAG, request) }

    suspend fun getAll() = repository.getHistory()
}