package com.code.overflow.demo.spring.repository

import com.code.overflow.demo.spring.model.Meter
import org.springframework.data.domain.{Page, Pageable}

trait MeterRepository {
  def add(meter: Meter): Meter
  def update(meter: Meter): Meter
  def list(pageable: Pageable): Page[Meter]
  def get(name: String): Option[Meter]
  def remove(meter: Meter): Unit
  def removeByName(meterName: String): Unit
  def filterByNameOrLabel(pattern: String, pageable: Pageable): Page[Meter]
}
