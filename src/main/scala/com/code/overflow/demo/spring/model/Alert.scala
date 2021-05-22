package com.code.overflow.demo.spring.model

import javax.persistence.{Column, Entity, Table}

@Entity
@Table(name = "alert")
class Alert extends BaseEvent {
  @Column(name = "label", nullable = false)
  var label: String = _

  @Column(name = "severity")
  var severity: Int = _

  override def toString = s"Meter($id, $label, $severity)"
}

object Alert {
  def apply(meter: Meter, label: String, severity: Int): Alert = {
    val alert = new Alert
    alert.meter = meter
    alert.label = label
    alert.severity = severity
    alert
  }
}
