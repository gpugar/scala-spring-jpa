package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.model.Alert
import com.code.overflow.demo.spring.repository.AlertRepository
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.{Propagation, Transactional}

import java.time.Instant
import java.util.{UUID, List => JList}
import scala.jdk.CollectionConverters._

@Transactional(propagation = Propagation.MANDATORY)
trait AlertRepositoryImpl extends AlertRepository with JpaRepository[Alert, UUID] {
  override def add(alert: Alert): Alert = save(alert)
  override def list(pageable: Pageable): Page[Alert] = findAll(pageable)
  override def listForMeterAndLabel(meterName: String, labelPattern: String, pageable: Pageable): Page[Alert] =
    findByMeterNameAndLabelContainingIgnoreCase(meterName, labelPattern, pageable)

  override def trendForMeterAndLabel(meterName: String, label: String, startDate: Instant, endDate: Option[Instant] = None): List[Alert] =
    endDate.map(e =>
      findAllByMeterNameAndLabelAndCreatedDateBetween(meterName, label,  startDate, e)
    ).getOrElse(findAllWithCreationDateTimeBefore(meterName, label, startDate)).asScala.toList

  protected def findByMeterNameAndLabelContainingIgnoreCase(meterName: String, label: String, pageable: Pageable): Page[Alert]
  protected def findAllByMeterNameAndLabelAndCreatedDateBetween(meterName: String, label: String, startDate: Instant, endDate: Instant): JList[Alert]
  @Query("select a from Alert a where a.meter.name = :meterName and a.label = :label and a.createdDate <= :createdDate")
  protected def findAllWithCreationDateTimeBefore(@Param("meterName") meterName: String,
                                        @Param("label") label: String,
                                        @Param("createdDate") createdDate: Instant): JList[Alert]
}
