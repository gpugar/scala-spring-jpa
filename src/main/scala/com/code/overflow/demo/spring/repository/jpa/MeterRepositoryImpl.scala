package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.model.Meter
import com.code.overflow.demo.spring.repository.MeterRepository
import org.springframework.data.domain.{Page, Pageable}
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param

import java.util.{Optional, UUID}
import scala.jdk.OptionConverters._

trait MeterRepositoryImpl extends MeterRepository with JpaRepository[Meter, UUID] {
  override def add(meter: Meter): Meter = save(meter)
  override def update(meter: Meter): Meter = save(meter)
  override def list(pageable: Pageable): Page[Meter] = findAll(pageable)
  override def remove(meter: Meter): Unit = delete(meter)
  override def removeByName(meterName: String): Unit = deleteByName(meterName)
  override def get(name: String): Option[Meter] = findByName(name).toScala
  override def filterByNameOrLabel(pattern: String, pageable: Pageable): Page[Meter] = findByNameOrLabel(pattern, pageable)

  protected def findByName(name: String): Optional[Meter]
  protected def deleteByName(name: String): Unit
  @Query("select m from Meter m where m.name like %:pattern% or m.label like %:pattern%")
  protected def findByNameOrLabel(@Param("pattern") pattern: String, pageable: Pageable): Page[Meter]
}
