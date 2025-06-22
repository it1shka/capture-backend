package com.it1shka.capture.database

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface DocumentUserAccessRepository : ReactiveCrudRepository<DocumentUserAccess, UUID> {
  fun findByUserId(userId: String): Flux<DocumentUserAccess>
  fun findByDocumentId(documentId: UUID): Flux<DocumentUserAccess>
  fun findByUserIdAndDocumentId(userId: String, documentId: UUID): Mono<DocumentUserAccess>
  fun deleteByDocumentId(documentId: UUID): Mono<Void>
}