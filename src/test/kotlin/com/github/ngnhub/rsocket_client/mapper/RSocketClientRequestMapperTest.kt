package com.github.ngnhub.rsocket_client.mapper

import com.github.ngnhub.rsocket_client.model.RSocketClientRequest
import com.github.ngnhub.rsocket_client.model.RSocketInputRequest
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

@ExtendWith(MockitoExtension::class)
class RSocketClientRequestMapperTest {

    @Mock
    private lateinit var request: ServerRequest

    @Mock
    private lateinit var validator: Validator

    @Mock
    private lateinit var constraint: ConstraintViolation<RSocketInputRequest?>

    private lateinit var mapper: RSocketClientRequestMapper

    @BeforeEach
    fun setUp() {
        mapper = RSocketClientRequestMapper(validator)
    }

    @Test
    fun `should map server request to RSocket client request from formMap`() {
        runBlocking {
            // given
            val formData = LinkedMultiValueMap<String, String>()
            val host = "localhost"
            val port = "1111"
            val route = "/some/route"
            formData.add("host", host)
            formData.add("port", port)
            formData.add("route", route)
            whenever(request.formData()).thenReturn(Mono.just(formData))

            val expected = RSocketClientRequest("localhost", port.toInt(), route)

            // when
            val actual = mapper.mapForm(request)

            // then
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should throw an error when constraints occurred in formMap`() {
        // given
        runBlocking {
            val formData = LinkedMultiValueMap<String, String>()
            whenever(request.formData()).thenReturn(Mono.just(formData))
            val constraints = mutableSetOf(constraint)
            whenever(validator.validate(any(RSocketInputRequest::class.java))).thenReturn(constraints)

            // when
            val exception = assertThrows(ConstraintViolationException::class.java)
            {
                runBlocking {
                    mapper.mapForm(request)
                }
            }

            // then
            assertEquals(constraints, exception.constraintViolations)
        }
    }

    @Test
    fun `should map server request to RSocket client request from json`() {
        runBlocking {
            // given
            val fromJson = RSocketInputRequest("localhost", 7070, "route")
            whenever(request.bodyToMono(RSocketInputRequest::class.java)).thenReturn(Mono.just(fromJson))

            // when
            val actual = mapper.mapJson(request)

            // then
            val expected = RSocketClientRequest("localhost", 7070, "route")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `should throw an error when constraints occurred in json`() {
        // given
        runBlocking {
            val fromJson = RSocketInputRequest("localhost", 7070, "route")
            whenever(request.bodyToMono(RSocketInputRequest::class.java)).thenReturn(Mono.just(fromJson))
            val constraints = mutableSetOf(constraint)
            whenever(validator.validate(any(RSocketInputRequest::class.java))).thenReturn(constraints)

            // when
            val exception = assertThrows(ConstraintViolationException::class.java)
            {
                runBlocking {
                    mapper.mapJson(request)
                }
            }

            // then
            assertEquals(constraints, exception.constraintViolations)
        }
    }
}
