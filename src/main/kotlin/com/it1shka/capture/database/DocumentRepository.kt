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
    LIMIT :pageSize 
    OFFSET :offset
  """)
  fun findByPrefixWithOffset(prefix: String, pageSize: Int, offset: Int): Flux<Document>

  fun findByPrefixWithPage(prefix: String, page: Int, pageSize: Int): Flux<Document> {
    val offset = page * pageSize
    return findByPrefixWithOffset(prefix, pageSize, offset)
  }
}