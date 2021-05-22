package com.code.overflow.demo.spring.model

import javax.persistence.{Column, Entity, Table}

@Entity
@Table(name = "meter")
class Meter extends BaseEntity {
  @Column(name = "name", unique = true, nullable = false)
  var name: String = _

  @Column(name = "label")
  var label: String = _

  override def toString = s"Meter($id, $name, $label)"
}

object Meter {
  def apply(name: String, label: Option[String] = None): Meter = {
    val meter = new Meter
    meter.name = name
    meter.label = label.orNull
    meter
  }
}