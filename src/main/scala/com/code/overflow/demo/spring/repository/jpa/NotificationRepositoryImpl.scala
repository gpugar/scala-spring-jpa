package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.model.Notification
import com.code.overflow.demo.spring.repository.NotificationRepository
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.{Propagation, Transactional}

import java.util.UUID

@Transactional(propagation = Propagation.MANDATORY)
trait NotificationRepositoryImpl extends NotificationRepository with JpaRepository[Notification, UUID] {
  override def add(alert: Notification): Notification = save(alert)
  override def list(pageable: Pageable): Page[Notification] = findAll(pageable)
  override def listForMeterAndLabel(meterName: String, labelPattern: String, pageable: Pageable): Page[Notification] =
    findByMeterNameAndLabelContainingIgnoreCase(meterName, labelPattern, pageable)

  protected def findByMeterNameAndLabelContainingIgnoreCase(meterName: String, label: String, pageable: Pageable): Page[Notification]
}
