package com.github.ngnhub.rsocket_client.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class CSVConfig {

    @Value("\${history.csv.path}")
    lateinit var path: String

    @PostConstruct
    fun initCSV() {
        val file = File(path)
        if (!file.exists()) {
            throw IllegalArgumentException("CSV file not found")
        }
    }
}
