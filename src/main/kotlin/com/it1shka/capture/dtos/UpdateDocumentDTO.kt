package com.it1shka.capture.dtos

data class UpdateDocumentDTO(
  val title: String?,
  val description: String?,
  val textContent: String?,
  val canvasContent: String?
)