package com.it1shka.capture.database

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("document_user_access")
data class DocumentUserAccess (
  @Id
  val id: UUID? = null,

  @Column("document_id")
  val documentId: UUID,

  @Column("user_id")
  val userId: String,

  @Column("role")
  val role: DocumentRole,
)