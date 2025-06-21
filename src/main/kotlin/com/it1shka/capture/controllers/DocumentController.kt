package com.it1shka.capture.controllers


import com.it1shka.capture.database.Document
import com.it1shka.capture.dtos.CreateDocumentDTO
import com.it1shka.capture.services.DocumentService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/document")
class DocumentController (
  private val documentService: DocumentService,
) {

  @PostMapping
  fun createDocument(
    @AuthenticationPrincipal jwt: Jwt,
    @RequestBody document: CreateDocumentDTO,
  ): Mono<Document> {
    val userId = jwt.subject
    return documentService.createDocument(document, userId)
  }

}