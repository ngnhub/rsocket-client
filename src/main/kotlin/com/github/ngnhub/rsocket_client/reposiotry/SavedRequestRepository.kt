package com.github.ngnhub.rsocket_client.reposiotry

import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedRequestRepository :
    CoroutineSortingRepository<SavedRequestEntity, Long>,
    CoroutineCrudRepository<SavedRequestEntity, Long>
