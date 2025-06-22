package com.it1shka.capture.controllers


import com.it1shka.capture.dtos.GenerateTokenDTO
import com.it1shka.capture.database.DocumentUserAccess
import com.it1shka.capture.services.AccessTokenService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/access-token")
class AccessTokenController (
  private val tokenService: AccessTokenService,
) {

  @PostMapping("/generate")
  fun generateToken(
    @AuthenticationPrincipal jwt: Jwt,
    @RequestBody token: GenerateTokenDTO,
  ): Mono<String> {
    val userId = jwt.subject
    val (documentId, accessLevel) = token
    return tokenService.generateToken(userId, documentId, accessLevel)
  }

  @PostMapping("/redeem")
  fun redeemToken(
    @AuthenticationPrincipal jwt: Jwt,
    @RequestBody token: String,
  ): Mono<DocumentUserAccess> {
    val userId = jwt.subject
    return tokenService.redeemToken(token, userId)
  }

}