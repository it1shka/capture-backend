package com.it1shka.capture.dtos

import com.fasterxml.jackson.databind.JsonNode
import java.util.UUID

data class UpdateDocumentDTO (
  val title: String?,
  val description: String?,
  val textContent: String?,
  val canvasContent: JsonNode?
)