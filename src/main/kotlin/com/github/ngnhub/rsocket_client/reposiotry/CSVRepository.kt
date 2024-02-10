package com.github.ngnhub.rsocket_client.reposiotry

import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import kotlin.reflect.full.declaredMemberProperties

@Service
class CSVRepository(@Value("\${history.csv.path}") private var path: String) {

    private val mutex = Mutex()

    companion object {
        private val headers = SavedRSocketRequest::class.declaredMemberProperties
            .map {
                it.name
            }.toTypedArray()
    }

    @PostConstruct
    fun initTable() {
        val file = File(path)
        if (!file.exists()) {
            throw IllegalArgumentException("CSV file not found")
        }
        CSVFormat.DEFAULT. print(file, StandardCharsets.UTF_8).use { // FIXME: it rewrites lines
            it.printRecord(
                "host",
                "port",
                "route",
                "savedAt"
            )
        }
    }

    suspend fun save(request: SavedRSocketRequest): RSocketInitRequest {
        mutex.lock()
        val file = File(path)
        CSVFormat.DEFAULT.print(file, StandardCharsets.UTF_8).use {
            with(request) {
                it .printRecord(
                    rSocketInitRequest.host,
                    rSocketInitRequest.port,
                    rSocketInitRequest.route,
                    savedAt.toString()
                )
                return rSocketInitRequest.also { mutex.unlock() }
            }
        }
    }

    suspend fun getHistory(): Flow<SavedRSocketRequest> {
        mutex.lock()
        val file = File(path)
        val inputStream = FileInputStream(file)
        return getCsvParser(inputStream).use { parser ->
            parser.drop(1)
                .map {
                    it.mapToSavedRSocketRequest()
                }.also {
                    mutex.unlock()
                }
                .asFlow()
        }
    }

    private fun getCsvParser(inputStream: FileInputStream): CSVParser =
        CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .apply {
                setIgnoreSurroundingSpaces(true)
            }
            .build().parse(inputStream.reader())

    private fun CSVRecord.mapToSavedRSocketRequest(): SavedRSocketRequest {
        val rSocketInitRequest = RSocketInitRequest(
            this[0],
            this[1].toInt(),
            this[2]
        )
        return SavedRSocketRequest(rSocketInitRequest, LocalDateTime.parse(this[3]))
    }
}
