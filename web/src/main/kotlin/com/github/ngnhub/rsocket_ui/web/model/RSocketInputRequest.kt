package com.github.ngnhub.rsocket_ui.web.model

import jakarta.validation.constraints.NotNull

data class RSocketInputRequest(
    @field:NotNull val host: String?,
    @field:NotNull val port: Int?,
    val route: String?
)