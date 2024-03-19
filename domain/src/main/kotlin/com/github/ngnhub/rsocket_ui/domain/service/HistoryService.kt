package com.github.ngnhub.rsocket_ui.domain.service

import com.github.ngnhub.rsocket_ui.domain.model.SavedRequest
import com.github.ngnhub.rsocket_ui.domain.model.SavedRequestEntity
import com.github.ngnhub.rsocket_ui.domain.reposiotry.SavedRequestRepository
import kotlinx.coroutines.flow.map
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class HistoryService(private val repository: SavedRequestRepository) {

    companion object {
        private const val LOG_TAG = "[HISTORY]"
        private val log = LoggerFactory.getLogger(HistoryService::class.java)
    }

    suspend fun save(request: SavedRequestEntity) =
        repository.save(request)
            .also { log.info("{} History item has been saved, {}", LOG_TAG, it) }

    suspend fun getAll() = repository
        .findAll(Sort.by(Sort.Order.desc("savedAt")))
        .map { it.toDto() }

    private fun SavedRequestEntity.toDto() = SavedRequest(id!!, host, port, route, savedAt)

    suspend fun deleteAll() = repository.deleteAll()
        .also { log.info("{} History has been cleared", LOG_TAG) }

    suspend fun deleteBy(id: Long) = repository.deleteById(id)
        .also { log.info("{} History item has been removed", LOG_TAG) }
}