package com.github.ngnhub.rsocket_client.error

data class RSocketListenerError(override val cause: Throwable) : RuntimeException()