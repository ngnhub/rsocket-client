package com.github.ngnhub.rsocket_client.service

import com.github.ngnhub.rsocket_client.model.RSocketInitRequest
import com.github.ngnhub.rsocket_client.model.SavedRSocketRequest
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
class CSVRepository {

    companion object {
        private val headers = SavedRSocketRequest::class.declaredMemberProperties
            .map {
                it.name
            }.toTypedArray()
    }

    @Value("\${history.csv.path}")
    lateinit var path: String

    fun SavedRSocketRequest.save(): RSocketInitRequest {
        val file = File(path)
        CSVFormat.DEFAULT.print(file, StandardCharsets.UTF_8).use {
            it.printRecord(*headers)
            it.printRecord(
                rSocketInitRequest.host,
                rSocketInitRequest.port,
                rSocketInitRequest.route,
                savedAt.toString()
            )
            return rSocketInitRequest
        }
    }

    fun getHistory(): List<SavedRSocketRequest> {
        val file = File(path)
        val inputStream = FileInputStream(file)
        return getCsvParser(inputStream).use { parser ->
            parser.drop(1)
                .map {
                    it.mapToSavedRSocketRequest()
                }
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
