package com.github.ngnhub.rsocket_ui.web.config

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
        val log : Logger = LoggerFactory.getLogger(com.github.ngnhub.rsocket_ui.web.config.CoroutineScopeConfig::class.java)
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
        com.github.ngnhub.rsocket_ui.web.config.CoroutineScopeConfig.Companion.log.info("Async task coroutine scope has been destroyed")
    }
}