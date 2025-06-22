package com.it1shka.capture.database

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface DocumentRepository : ReactiveCrudRepository<Document, UUID> {
  @Query("""
    select
        doc.*
    from documents doc
    join document_user_access access
        on doc.id = access.document_id
    where
        access.user_id = :userId and
        lower(doc.title) like concat(lower(:titlePrefix), '%')
    order by doc.updated_at desc
    limit :limit
    offset :offset
  """)
  fun performSearch(
    userId: String,
    titlePrefix: String,
    limit: Int,
    offset: Int
  ): Flux<Document>
}