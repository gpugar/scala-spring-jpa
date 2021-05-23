package com.code.overflow.demo.spring.model

import org.hibernate.annotations.Type

import java.util.UUID
import javax.persistence._

@MappedSuperclass
abstract class BaseEntity extends Serializable {
  @Id
  @GeneratedValue
  @Column(name = "id", length = 36)
  @Type(`type` = "uuid-char")
  var id: UUID = _
}
