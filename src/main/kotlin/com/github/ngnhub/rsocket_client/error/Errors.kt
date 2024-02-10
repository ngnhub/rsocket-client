package com.github.ngnhub.rsocket_client.error

class Errors(private val messages: List<String> = mutableListOf()) {
        operator fun plus(message: String) = messages.addLast(message)

        fun isNotEmpty() = messages.isNotEmpty()

        override fun toString(): String {
            return messages.reduce { current: String, next: String ->
                "$current\n$next"
            }
        }
    }
