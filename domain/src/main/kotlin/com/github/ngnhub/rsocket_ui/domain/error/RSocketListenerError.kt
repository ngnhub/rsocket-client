package com.github.ngnhub.rsocket_ui.domain.error

data class RSocketListenerError(override val message: String?) : RuntimeException()