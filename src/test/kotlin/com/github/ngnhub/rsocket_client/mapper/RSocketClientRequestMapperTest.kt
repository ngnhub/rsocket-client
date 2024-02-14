package com.github.ngnhub.rsocket_client.mapper

import com.github.ngnhub.rsocket_client.error.ValidationException
import com.github.ngnhub.rsocket_client.model.RSocketClientRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

@ExtendWith(MockitoExtension::class)
class RSocketClientRequestMapperTest {

    @Mock
    lateinit var request: ServerRequest

    @Test
    fun `should map server request to RSocket init request`() {
        runBlocking {
            // given
            val formData = LinkedMultiValueMap<String, String>()
            val host = "localhost"
            val port = "1111"
            val route = "/some/route"
            formData.add("host", host)
            formData.add("port", port)
            formData.add("route", route)
            `when`(request.formData()).thenReturn(Mono.just(formData))

            val expected = RSocketClientRequest("localhost", port.toInt(), route)

            // when
            val actual = RSocketInputRequestMapper.mapForm(request)

            // then
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should throw an error with multiple violations`() {
        // given
        val formData = LinkedMultiValueMap<String, String>()
        `when`(request.formData()).thenReturn(Mono.just(formData))

        // when
        val exception =
            assertThrows(ValidationException::class.java) { runBlocking { RSocketInputRequestMapper.mapForm(request) } }

        // then
        assertEquals(
            "'host' can not be empty\n'port' can not be empty\n'path' can not be empty", exception.message
        )
    }

    @Test
    fun `should throw an error when port is not numeric`() {
        // given
        val formData = LinkedMultiValueMap<String, String>()
        val host = "localhost"
        val port = "aaaa"
        val route = "/some/route"
        formData.add("host", host)
        formData.add("port", port)
        formData.add("route", route)
        `when`(request.formData()).thenReturn(Mono.just(formData))

        // when
        val exception =
            assertThrows(ValidationException::class.java) { runBlocking { RSocketInputRequestMapper.mapForm(request) } }

        // then
        assertEquals("'port' must have a numeric format", exception.message)
    }
}
