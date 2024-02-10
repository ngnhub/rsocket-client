package com.github.ngnhub.rsocket_client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RsocketClientApplication

fun main(args: Array<String>) {
    runApplication<RsocketClientApplication>(*args)
}
