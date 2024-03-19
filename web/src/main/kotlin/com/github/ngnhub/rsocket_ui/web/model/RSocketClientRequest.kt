package com.github.ngnhub.rsocket_ui.web.model

data class RSocketClientRequest(
    val host: String,
    val port: Int,
    val route: String?
)
