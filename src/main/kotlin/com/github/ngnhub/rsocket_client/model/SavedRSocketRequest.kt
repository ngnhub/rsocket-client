package com.github.ngnhub.rsocket_client.model

import java.time.LocalDateTime

data class SavedRSocketRequest(
    val rSocketInitRequest: RSocketInitRequest,
    val savedAt: LocalDateTime = LocalDateTime.now()
)
