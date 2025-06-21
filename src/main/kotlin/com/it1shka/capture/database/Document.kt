package com.it1shka.capture.database

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("documents")
data class Document (
  @Id
  val id: UUID? = null,

  @Column("title")
  val title: String,

  @Column("description")
  val description: String? = null,

  @Column("text_content")
  val textContent: String? = null,

  @Column("canvas_content")
  val canvasContent: JsonNode? = null,

  @CreatedDate
  @Column("created_at")
  val createdAt: Instant? = null,

  @LastModifiedDate
  @Column("updated_at")
  val updatedAt: Instant? = null
)