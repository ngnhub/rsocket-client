package com.github.ngnhub.rsocket_client.config

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineScopeConfig {

    companion object {
        val log : Logger = LoggerFactory.getLogger(CoroutineScopeConfig::class.java)
    }

    lateinit var scope: CoroutineScope

    @Bean
    fun asyncTaskCoroutineScope() : CoroutineScope {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        scope = coroutineScope
        return coroutineScope
    }

    @PreDestroy
    fun tearDown() {
        scope.cancel()
        log.info("Async task coroutine scope has been destroyed")
    }
}
