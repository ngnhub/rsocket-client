package com.github.ngnhub.rsocket_client.utils

import jakarta.annotation.PostConstruct
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

// TODO: mapdb
@Component
class CsvBridge(@Value("\${history.csv.path}") private var path: String) {


    @PostConstruct
    fun initFile() {
        val file = File(path)
        if (!file.exists()) {
            throw IllegalArgumentException("CSV file not found")
        }
        var size: Int
        getCsvParser().use {
            size = it.count()
        }
        if (size == 0) {
            val csvPrinter = getCsvPrinter()
            csvPrinter.use {
                it.printRecord("host", "port", "route", "savedAt")
            }
        }
    }

    fun getCsvPrinter(): CSVPrinter =
        CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .build()
            .print(getWriter())

    fun getCsvParser(): CSVParser =
        CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .apply {
                setSkipHeaderRecord(true)
                setIgnoreSurroundingSpaces(true)
            }
            .build().parse(FileInputStream(File(path)).reader())

    private fun getWriter(): BufferedWriter {
        return Files.newBufferedWriter(
            Paths.get(path),
            StandardOpenOption.APPEND,
            StandardOpenOption.CREATE
        )
    }
}