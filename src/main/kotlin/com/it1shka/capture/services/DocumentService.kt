package com.it1shka.capture.services

import com.it1shka.capture.database.Document
import com.it1shka.capture.database.DocumentRepository
import com.it1shka.capture.database.DocumentRole
import com.it1shka.capture.database.DocumentUserAccess
import com.it1shka.capture.database.DocumentUserAccessRepository
import com.it1shka.capture.dtos.CreateDocumentDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class DocumentService (
  private val documentRepository: DocumentRepository,
  private val documentUserAccessRepository: DocumentUserAccessRepository,
) {

  fun createDocument(document: CreateDocumentDTO, userId: String): Mono<Document> {
    val document = Document(
      title = document.title,
      description = document.description,
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