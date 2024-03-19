package com.github.ngnhub.rsocket_ui.web.error

data class RSocketListenerError(override val message: String?) : RuntimeException()