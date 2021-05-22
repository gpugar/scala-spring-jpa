package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.model.Alert
import com.code.overflow.demo.spring.repository.AlertRepository
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.{Propagation, Transactional}

import java.util.UUID

@Transactional(propagation = Propagation.MANDATORY)
trait AlertRepositoryImpl extends AlertRepository with JpaRepository[Alert, UUID] {
  override def add(alert: Alert): Alert = save(alert)
  override def list(pageable: Pageable): Page[Alert] = findAll(pageable)
  override def listForMeterAndLabel(meterName: String, labelPattern: String, pageable: Pageable): Page[Alert] =
    findByMeterNameAndLabelContainingIgnoreCase(meterName, labelPattern, pageable)

  protected def findByMeterNameAndLabelContainingIgnoreCase(meterName: String, label: String, pageable: Pageable): Page[Alert]
}
