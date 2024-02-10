package com.github.ngnhub.rsocket_client.error

class ValidationException(override val message: String) : RuntimeException(message)
