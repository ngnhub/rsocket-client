package com.github.ngnhub.rsocket_client.service

import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
import com.github.ngnhub.rsocket_client.reposiotry.CSVRepository
import org.springframework.stereotype.Service

@Service
class HistoryService(private val repository: CSVRepository) {

    fun save(request: SavedRSocketRequest) = repository.save(request)

    fun getAll() = repository.getHistory()
}