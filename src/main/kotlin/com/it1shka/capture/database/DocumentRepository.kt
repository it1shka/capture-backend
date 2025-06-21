package com.it1shka.capture.database

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface DocumentRepository : ReactiveCrudRepository<Document, UUID> {
  @Query("""
    SELECT 
        *
    FROM documents
    WHERE lower(title) LIKE lower(:prefix) || '%' 
    ORDER BY title 
    LIMIT :limit
    OFFSET :offset
  """)
  fun findByTitleStartingWithIgnoreCase(prefix: String, limit: Int, offset: Int): Flux<Document>
}