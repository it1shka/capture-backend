package com.it1shka.capture.dtos

import java.util.UUID
import com.it1shka.capture.database.DocumentRole

data class GenerateTokenDTO (
  val documentId: UUID,
  val accessLevel: DocumentRole
)