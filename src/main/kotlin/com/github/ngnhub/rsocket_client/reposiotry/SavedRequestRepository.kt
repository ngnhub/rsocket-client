package com.github.ngnhub.rsocket_client.reposiotry

import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedRequestRepository : ReactiveCrudRepository<SavedRequestEntity, Long>
