package com.it1shka.capture.database


import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

enum class DocumentRole {
  AUTHOR,
  EDITOR,
  VIEWER;
  
  fun canEdit() : Boolean = this == AUTHOR || this == EDITOR
  fun canDelete() : Boolean = this == AUTHOR

  fun getRank(): Int {
        return when (this) {
            AUTHOR -> 2
            EDITOR -> 1
            VIEWER -> 0
        }
    }

}

@Component
@ReadingConverter
class DocumentRoleReadingConverter : Converter<String, DocumentRole> {
  override fun convert(source: String): DocumentRole {
    return DocumentRole.valueOf(source.uppercase())
  }
}

@Component
@WritingConverter
class DocumentRoleWritingConverter : Converter<DocumentRole, String> {
  override fun convert(source: DocumentRole): String {
    return source.name
  }
}