package com.code.overflow.demo.spring.model

import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate

import java.time.Instant
import java.util.UUID
import javax.persistence.{Column, GeneratedValue, Id, MappedSuperclass}

@MappedSuperclass
abstract class BaseEntity extends Serializable {
  @Id
  @GeneratedValue
  @Column(name = "\"ID\"", length = 36)
  @Type(`type` = "uuid-char")
  var id: UUID = _

  @CreatedDate
  var createdDate: Instant = _
}
