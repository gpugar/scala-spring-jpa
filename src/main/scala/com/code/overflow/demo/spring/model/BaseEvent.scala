package com.code.overflow.demo.spring.model

import org.hibernate.annotations.Type

import javax.persistence.{FetchType, JoinColumn, ManyToOne, MappedSuperclass}

@MappedSuperclass
abstract class BaseEvent extends BaseEntity {
  @Type(`type` = "uuid-char")
  @JoinColumn(name = "meter_id")
  @ManyToOne(fetch = FetchType.LAZY)
  var meter: Meter = _
}
