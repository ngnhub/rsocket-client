package com.github.ngnhub.rsocket_ui.domain.reposiotry

import com.github.ngnhub.rsocket_ui.domain.model.SavedRequestEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedRequestRepository :
    CoroutineSortingRepository<SavedRequestEntity, Long>,
    CoroutineCrudRepository<SavedRequestEntity, Long>
