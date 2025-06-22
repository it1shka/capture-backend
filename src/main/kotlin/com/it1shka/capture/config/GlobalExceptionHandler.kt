package com.it1shka.capture.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
  val message: String,
  val timestamp: Long = System.currentTimeMillis()
)

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(SecurityException::class)
  fun handleSecurityException(ex: SecurityException): ResponseEntity<ErrorResponse> {
    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(ErrorResponse(ex.message ?: "Access denied"))
  }

  @ExceptionHandler(NoSuchElementException::class)
  fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<ErrorResponse> {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(ErrorResponse(ex.message ?: "Resource not found"))
  }
}
