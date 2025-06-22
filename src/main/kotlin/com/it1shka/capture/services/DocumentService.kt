package com.it1shka.capture.services

import com.it1shka.capture.database.Document
import com.it1shka.capture.database.DocumentRepository
import com.it1shka.capture.database.DocumentRole
import com.it1shka.capture.database.DocumentUserAccess
import com.it1shka.capture.database.DocumentUserAccessRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

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

  fun updateDocument(userId: String, documentId: UUID, title: String? = null, description: String? = null, textContent: String? = null, canvasContent: JsonNode? = null): Mono<Document> {
    return documentRepository.findById(documentId)
      .switchIfEmpty(Mono.error(NoSuchElementException("Document not found")))
      .flatMap { existingDocument ->
        documentUserAccessRepository.findByUserIdAndDocumentId(userId, documentId)
          .filter { access -> access.role.canEdit() }
          .switchIfEmpty(Mono.error(SecurityException("User doesn't have permission")))
          .thenReturn(existingDocument)
      }
      .flatMap { existingDocument ->
        val updatedDocument = existingDocument.copy(
          title = title ?: existingDocument.title,
          description = description ?: existingDocument.description,
          textContent = textContent ?: existingDocument.textContent,
          canvasContent = canvasContent ?: existingDocument.canvasContent
        )
        documentRepository.save(updatedDocument)
      }
  }

  fun deleteDocument(userId: String, documentId: UUID): Mono<Unit> {
    return documentRepository.findById(documentId)
      .switchIfEmpty(Mono.error(NoSuchElementException("Document not found")))
      .flatMap { document ->
        documentUserAccessRepository.findByUserIdAndDocumentId(userId, documentId)
          .filter { access -> access.role.canDelete() }
          .switchIfEmpty(Mono.error(SecurityException("User doesn't have permission")))
          .then(
            documentUserAccessRepository.deleteByDocumentId(documentId)
             .then(documentRepository.delete(document))
          )
      }
  }

}