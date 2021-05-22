package com.code.overflow.demo.spring.repository

import com.code.overflow.demo.spring.model.Alert
import org.springframework.data.domain.{Page, Pageable}

import java.time.Instant

trait AlertRepository {
  def add(alert: Alert): Alert
  def list(pageable: Pageable): Page[Alert]
  def listForMeterAndLabel(meterName: String, labelPattern: String, pageable: Pageable): Page[Alert]
  def trendForMeterAndLabel(meterName: String, label: String, startDate: Instant, endDate: Option[Instant] = None): List[Alert]
}
