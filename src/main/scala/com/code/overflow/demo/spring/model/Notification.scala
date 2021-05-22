package com.code.overflow.demo.spring.model

import javax.persistence.{Column, Entity, Table}

@Entity
@Table(name = "notification")
class Notification extends BaseEvent {
  @Column(name = "label", nullable = false)
  var label: String = _

  @Column(name = "meter_value")
  var meterValue: Int = _
}

object Notification {
  def apply(meter: Meter, label: String, meterValue: Int): Notification = {
    val notification = new Notification
    notification.meter = meter
    notification.label = label
    notification.meterValue = meterValue
    notification
  }
}
