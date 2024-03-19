package com.github.ngnhub.rsocket_ui.web.reposiotry

import com.github.ngnhub.rsocket_ui.web.model.SavedRequestEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedRequestRepository :
    CoroutineSortingRepository<SavedRequestEntity, Long>,
    CoroutineCrudRepository<SavedRequestEntity, Long>
