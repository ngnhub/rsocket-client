package com.github.ngnhub.rsocket_client.reposiotry

import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
import com.github.ngnhub.rsocket_client.utils.CsvBridge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import java.time.LocalDateTime

// TODO: cache
@Service
class CSVRepository(val bridge: CsvBridge) {

    private val mutex = Mutex()

    suspend fun save(request: SavedRSocketRequest): RSocketInitRequest {
        mutex.lock()
        bridge.getCsvPrinter().use {
            with(request) {
                it.printRecord(
                    rSocketInitRequest.host,
                    rSocketInitRequest.port,
                    rSocketInitRequest.route,
                    savedAt.toString()
                )
                return rSocketInitRequest
                    .also { mutex.unlock() }
            }
        }
    }

    suspend fun getHistory(): Flow<SavedRSocketRequest> {
        mutex.lock()
        return bridge.getCsvParser().use { parser ->
            parser.drop(1)
                .map {
                    it.mapToSavedRSocketRequest()
                }.also {
                    mutex.unlock()
                }
                .reversed()
                .asFlow()
        }
    }

    private fun CSVRecord.mapToSavedRSocketRequest(): SavedRSocketRequest {
        val rSocketInitRequest = RSocketInitRequest(
            this[0],
            this[1].toInt(),
            this[2]
        )
        return SavedRSocketRequest(rSocketInitRequest, LocalDateTime.parse(this[3]))
    }
}
