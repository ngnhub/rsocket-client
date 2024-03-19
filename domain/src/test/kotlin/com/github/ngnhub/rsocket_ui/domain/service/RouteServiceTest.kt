package com.github.ngnhub.rsocket_ui.domain.service

import com.github.ngnhub.rsocket_ui.domain.client.RsocketListener
import com.github.ngnhub.rsocket_ui.domain.model.RSocketClientRequest
import com.github.ngnhub.rsocket_ui.domain.model.SavedRequestEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ExtendWith(MockitoExtension::class)
class RouteServiceTest {

    @Mock
    private lateinit var listener: RsocketListener

    @Mock
    private lateinit var historyService: HistoryService

    @Mock
    private lateinit var clock: Clock

    private val asyncTaskCoroutineScope = TestScope()

    private lateinit var service: RouteService

    @BeforeEach
    fun setUp() {
        clock = Clock.fixed(Instant.parse("2025-01-01T01:01:00Z"), ZoneId.of("UTC"))
        service = RouteService(listener, historyService, asyncTaskCoroutineScope, clock)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun route() {
        runTest {
            // given
            val request = RSocketClientRequest("host", "1111".toInt(), "route")
            val expected = listOf("rsocket_value", "rsocket_value2")
            whenever(listener.request(request)).thenReturn(expected.asFlow())

            // when
            val actual = service.route(request)

            // then
            assertEquals(expected, actual.toList())
            verify(listener).request(request)

            asyncTaskCoroutineScope.advanceUntilIdle()
            val dateTime = LocalDateTime.of(2025, 1, 1, 1, 1)
            val expectedSaved = SavedRequestEntity(null, request.host, request.port, request.route!!, dateTime)
            verify(historyService).save(expectedSaved)
        }
    }
}
