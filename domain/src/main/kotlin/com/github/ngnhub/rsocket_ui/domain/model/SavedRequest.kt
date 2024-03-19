package com.github.ngnhub.rsocket_ui.domain.model

import java.time.LocalDateTime

data class SavedRequest(
    val id: Long,
    val host: String,
    val port: Int,
    val route: String,
    val savedAt: LocalDateTime
)
