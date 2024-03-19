package com.github.ngnhub.rsocket_client.domain.error

data class RSocketListenerError(override val message: String?) : RuntimeException()