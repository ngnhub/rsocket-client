package com.github.ngnhub.rsocket_ui.web.service

import com.github.ngnhub.rsocket_ui.web.model.SavedRequest
import com.github.ngnhub.rsocket_ui.web.model.SavedRequestEntity
import com.github.ngnhub.rsocket_ui.web.reposiotry.SavedRequestRepository
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Sort
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class HistoryServiceTest {

    @Mock
    private lateinit var repository: SavedRequestRepository
    private lateinit var service: HistoryService

    @BeforeEach
    fun setUp() {
        service = HistoryService(repository)
    }

    @Test
    fun save() {
        runBlocking {
            // given
            val date = LocalDateTime.of(2025, 1, 1, 1, 1)
            val entity = SavedRequestEntity(1L, "host", 1111, "route", date)

            // when
            service.save(entity)

            // then
            verify(repository).save(entity)
        }
    }

    @Test
    fun getAll() {
        runBlocking {
            // given
            val date1 = LocalDateTime.of(2025, 1, 1, 1, 1)
            val date2 = LocalDateTime.of(2025, 1, 1, 1, 2)
            val date3 = LocalDateTime.of(2025, 1, 1, 1, 2)
            val entity1 = SavedRequestEntity(1L, "host", 1111, "route", date1)
            val entity2 = SavedRequestEntity(1L, "host", 1111, "route", date2)
            val entity3 = SavedRequestEntity(1L, "host", 1111, "route", date3)
            val repositoryResponse = listOf(entity1, entity2, entity3).asFlow()
            whenever(repository.findAll(any())).thenReturn(repositoryResponse)

            val dto1 = SavedRequest(1L, "host", 1111, "route", date1)
            val dto2 = SavedRequest(1L, "host", 1111, "route", date2)
            val dto3 = SavedRequest(1L, "host", 1111, "route", date3)
            val expected = listOf(dto1, dto2, dto3)

            // when
            val actual = service.getAll().toList()

            // then
            assertEquals(expected, actual)
            verify(repository).findAll(Sort.by(Sort.Order.desc("savedAt")))
        }
    }

    @Test
    fun `should delete all history`() {
        runBlocking {
            // when
            service.deleteAll()

            //then
            verify(repository).deleteAll()
        }
    }

    @Test
    fun `should delete history item`() {
        runBlocking {
            // when
            service.deleteBy(1L)

            //then
            verify(repository).deleteById(1L)
        }
    }
}