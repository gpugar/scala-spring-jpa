package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.model.Notification
import com.code.overflow.demo.spring.repository.NotificationRepository
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param

import java.time.Instant
import java.util.{UUID, List => JList}
import scala.jdk.CollectionConverters._

trait NotificationRepositoryImpl extends NotificationRepository with JpaRepository[Notification, UUID] {

  override def add(alert: Notification): Notification = save(alert)
  override def update(alert: Notification): Notification = save(alert)
  override def list(pageable: Pageable): Page[Notification] = findAll(pageable)
  override def listForMeterAndLabel(meterName: String, labelPattern: String, pageable: Pageable): Page[Notification] =
    findByMeterNameAndLabelContainingIgnoreCase(meterName, labelPattern, pageable)

  override def trendForMeterAndLabel(meterName: String, label: String, startDate: Instant, endDate: Option[Instant] = None): List[Notification] =
    endDate.map(e =>
      findAllByMeterNameAndLabelAndCreatedDateBetween(meterName, label,  startDate, e)
    ).getOrElse(findAllWithCreationDateTimeBefore(meterName, label, startDate)).asScala.toList

  protected def findByMeterNameAndLabelContainingIgnoreCase(meterName: String, label: String, pageable: Pageable): Page[Notification]
  protected def findAllByMeterNameAndLabelAndCreatedDateBetween(meterName: String, label: String, startDate: Instant, endDate: Instant): JList[Notification]
  @Query("select n from Notification n where n.meter.name = :meterName and n.label = :label and n.createdDate <= :createdDate")
  protected def findAllWithCreationDateTimeBefore(@Param("meterName") meterName: String,
                                                  @Param("label") label: String,
                                                  @Param("createdDate") createdDate: Instant): JList[Notification]
}
