package com.github.ngnhub.rsocket_ui.web.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("SAVED_REQUESTS")
data class SavedRequestEntity(
    @Id val id: Long?,
    val host: String,
    val port: Int,
    val route: String,
    val savedAt: LocalDateTime
)
