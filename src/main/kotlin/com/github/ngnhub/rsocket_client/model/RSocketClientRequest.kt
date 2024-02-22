package com.github.ngnhub.rsocket_client.model

data class RSocketClientRequest(
    val host: String,
    val port: Int,
    val route: String?
)
