package com.github.ngnhub.rsocket_client.reposiotry

import com.github.ngnhub.rsocket_client.model.SavedRequestEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedRequestRepository : R2dbcRepository<SavedRequestEntity, Long>
