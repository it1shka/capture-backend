package com.it1shka.capture.services

import com.it1shka.capture.database.Document
import com.it1shka.capture.database.DocumentRepository
import com.it1shka.capture.database.DocumentRole
import com.it1shka.capture.database.DocumentUserAccess
import com.it1shka.capture.database.DocumentUserAccessRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DocumentService(
  private val documentRepository: DocumentRepository,
  private val documentUserAccessRepository: DocumentUserAccessRepository,
) {
  fun readDocuments(
    userId: String,
    titlePrefix: String,
    page: Int,
    pageSize: Int
  ): Flux<Document> {
    val offset = page * pageSize
    return documentRepository.performSearch(
      userId = userId,
      titlePrefix = titlePrefix,
      limit = pageSize,
      offset = offset,
    )
  }

  fun createDocument(userId: String, title: String, description: String? = null): Mono<Document> {
    val document = Document(
      title = title,
      description = description,
    )
    return documentRepository.save(document).flatMap { savedDocument ->
      val documentUserAccess = DocumentUserAccess(
        documentId = savedDocument.id!!,
        userId = userId,
        role = DocumentRole.AUTHOR,
      )
      documentUserAccessRepository
        .save(documentUserAccess)
        .thenReturn(savedDocument)
    }
  }

}