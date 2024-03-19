package com.github.ngnhub.rsocket_client.web

import com.github.ngnhub.rsocket_client.domain.config.DomainAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [DomainAutoConfiguration::class])
class RsocketClientApplication

fun main(args: Array<String>) {
    runApplication<RsocketClientApplication>(*args)
}
