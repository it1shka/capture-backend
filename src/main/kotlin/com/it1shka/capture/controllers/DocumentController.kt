package com.it1shka.capture.controllers


import com.it1shka.capture.database.Document
import com.it1shka.capture.dtos.CreateDocumentDTO
import com.it1shka.capture.dtos.UpdateDocumentDTO
import com.it1shka.capture.services.DocumentService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

private const val DEFAULT_SEARCH = ""
private const val DEFAULT_PAGE = 0
private const val DEFAULT_PAGE_SIZE = 20

@RestController
@RequestMapping("/document")
class DocumentController(
  private val documentService: DocumentService,
) {

  @GetMapping
  fun readDocuments(
    @AuthenticationPrincipal jwt: Jwt,
    @RequestParam search: String = DEFAULT_SEARCH,
    @RequestParam page: Int = DEFAULT_PAGE,
    @RequestParam pageSize: Int = DEFAULT_PAGE_SIZE
  ): Flux<Document> {
    val userId = jwt.subject
    return documentService.readDocuments(userId, search, page, pageSize)
  }

  @GetMapping("/{id}")
  fun readDocument(
    @AuthenticationPrincipal jwt: Jwt,
    @PathVariable id: UUID,
  ): Mono<Document> {
    val userId = jwt.subject
    return documentService.readDocument(userId, id)
  }

  @GetMapping("/{id}/permission")
  fun readDocumentPermission(
    @AuthenticationPrincipal jwt: Jwt,
    @PathVariable id: UUID,
  ): Mono<String> {
    val userId = jwt.subject
    return documentService.readDocumentPermission(userId, id)
      .map { it.name }
  }

  @PostMapping
  fun createDocument(
    @AuthenticationPrincipal jwt: Jwt,
    @RequestBody document: CreateDocumentDTO,
  ): Mono<Document> {
    val userId = jwt.subject
    val (title, description) = document
    return documentService.createDocument(userId, title, description)
  }

  @PutMapping("/{id}")
  fun updateDocument(
    @AuthenticationPrincipal jwt: Jwt,
    @PathVariable id: UUID,
    @RequestBody document: UpdateDocumentDTO,
  ): Mono<Document> {
    val userId = jwt.subject
    val (title, description, textContent, canvasContent) = document
    return documentService.updateDocument(userId, id, title, description, textContent, canvasContent)
  }

}