package com.github.ngnhub.rsocket_client.model

data class RSocketInitRequest(
    val host: String,
    val port: Int,
    val route: String
)
