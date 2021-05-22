package com.code.overflow.demo.spring

import com.code.overflow.demo.spring.model.{Alert, Meter, Notification}
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.{DataJpaTest, TestEntityManager}
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

@RunWith(classOf[SpringRunner])
@DataJpaTest
@Transactional
abstract class BaseScalaJpaTest
    extends AnyFunSpecLike
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with RollbackEachTransactionalTest {
  @Autowired
  override var transactionManager: PlatformTransactionManager = _

  @Autowired
  var transactionTemplate: TransactionTemplate = _

  @Autowired
  var entityManager: TestEntityManager = _

  @Autowired
  var jdbcTemplate: JdbcTemplate = _

  val manager = new TestContextManager(this.getClass)

  override protected def beforeAll(): Unit =
    manager.prepareTestInstance(this)

  override protected def beforeEach(): Unit =
    initTest()

  override protected def afterAll(): Unit =
    manager.afterTestClass()

  def persist[E](e: E): E =
    transactionTemplate.execute(_ => entityManager.persist(e))

  val temporalMap = Map(
    (1, createLocalDateTime("2021-01-01 01:00")),
    (2, createLocalDateTime("2021-01-01 02:00")),
    (3, createLocalDateTime("2021-01-01 03:00")),
    (4, createLocalDateTime("2021-01-01 04:00")),
    (5, createLocalDateTime("2021-01-01 05:00")),
    (6, createLocalDateTime("2021-01-01 06:00"))
  )

  private def initTest(): Unit = {
    val meters = (1 to 10).toList
      .map(x => Meter("meter_" + x, Option("label_of_" + x)))
      .map(persist)
    meters.foreach(meter => {
      (1 to 4).toList.map(x => {
        Alert(meter, "alert", x)
      }).map(persist)
      (1 to 6).toList.map(x => {
        Notification(meter, "notification1", x)
      }).map(persist)
      (1 to 2).toList.map(x => {
        Notification(meter, "notification2", x)
      }).map(persist)
    })
  }

  private def createLocalDateTime(date: String): TemporalAccessor =
    ZonedDateTime.of(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), ZoneId.systemDefault)
}
