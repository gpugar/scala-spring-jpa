package com.code.overflow.demo.spring.repository

import com.code.overflow.demo.spring.model.Notification
import org.springframework.data.domain.{Page, Pageable}

trait NotificationRepository {
  def add(notification: Notification): Notification
  def list(pageable: Pageable): Page[Notification]
  def listForMeterAndLabel(meterName: String, labelPattern: String, pageable: Pageable): Page[Notification]
}
