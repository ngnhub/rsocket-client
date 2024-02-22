package com.github.ngnhub.rsocket_client.model

import jakarta.validation.constraints.NotNull

data class RSocketInputRequest(
    @field:NotNull val host: String?,
    @field:NotNull val port: Int?,
    val route: String?
)