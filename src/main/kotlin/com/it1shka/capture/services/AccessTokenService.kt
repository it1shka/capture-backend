package com.it1shka.capture.services

import com.it1shka.capture.database.Document
import com.it1shka.capture.database.DocumentRepository
import com.it1shka.capture.database.DocumentRole
import com.it1shka.capture.database.DocumentUserAccess
import com.it1shka.capture.database.DocumentUserAccessRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.util.*
import kotlin.text.Charsets
import kotlin.text.padEnd
import org.springframework.beans.factory.annotation.Value

@Service
class AccessTokenService(
  private val documentRepository: DocumentRepository,
  private val documentUserAccessRepository: DocumentUserAccessRepository,
  
  @Value("\${INVITE_SECRET}") // should be from env, but i might have done this a bit wrong, not too sure
  private val secret: String
) {

  private val algorithm = "AES"
  private val key = SecretKeySpec(secret.padEnd(32).take(32).toByteArray(Charsets.UTF_8), algorithm)

  fun generateToken(userId: String, documentId: UUID, accessLevel: DocumentRole): Mono<String> {
    return documentRepository.findById(documentId)
      .switchIfEmpty(Mono.error(NoSuchElementException("Document not found")))
      .flatMap { _ ->
        documentUserAccessRepository.findByUserIdAndDocumentId(userId, documentId)
          .filter { it.role.canDelete() }
          .switchIfEmpty(Mono.error(SecurityException("User doesn't have permission")))
          .then(Mono.fromCallable {
            val payload = "$documentId|${accessLevel.name}"
            Base64.getUrlEncoder().encodeToString(
                Cipher.getInstance(algorithm).apply {
                    init(Cipher.ENCRYPT_MODE, key)
                }.doFinal(payload.toByteArray())
            )
          })
      }
  }

  fun redeemToken(token: String, redeemingUserId: String): Mono<DocumentUserAccess> {
    return Mono.fromCallable {
      val decrypted = String(
        Cipher.getInstance(algorithm).apply {
          init(Cipher.DECRYPT_MODE, key)
        }.doFinal(Base64.getUrlDecoder().decode(token))
      )
      decrypted.split("|").let { parts ->
        UUID.fromString(parts[0]) to DocumentRole.valueOf(parts[1])
      }
    }.onErrorMap { 
      IllegalArgumentException("Invalid token format") 
    }.flatMap { (documentId, role) ->
      documentRepository.findById(documentId)
        .switchIfEmpty(Mono.error(NoSuchElementException("Document not found")))
        .flatMap { document ->
          documentUserAccessRepository.findByUserIdAndDocumentId(redeemingUserId, documentId)
            .switchIfEmpty(
              // Create new access if none exists
              documentUserAccessRepository.save(
                DocumentUserAccess(
                  documentId = documentId,
                  userId = redeemingUserId,
                  role = role
                )
              )
            )
            .flatMap { existingAccess ->
              // Only modify if new role has higher privileges (lower rank)
              if (role.getRank() < existingAccess.role.getRank()) {
                documentUserAccessRepository.save(
                  existingAccess.copy(role = role)
                )
              } else {
                Mono.just(existingAccess)
              }
            }
        }
    }
  }

}