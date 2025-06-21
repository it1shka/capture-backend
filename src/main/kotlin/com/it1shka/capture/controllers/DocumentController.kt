package com.it1shka.capture.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/document")
class DocumentController {
  @GetMapping
  fun getDocuments(@AuthenticationPrincipal jwt: Jwt): Mono<ResponseEntity<String>> {
    // TODO:
    return Mono.just(ResponseEntity.ok(jwt.id))
  }
}