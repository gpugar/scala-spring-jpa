package com.code.overflow.demo.spring

import com.code.overflow.demo.spring.model.{Alert, Meter, Notification}
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.{DataJpaTest, TestEntityManager}
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.auditing.AuditingHandler
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate

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

  @MockBean
  var auditingHandler: AuditingHandler = _

  override protected def beforeAll(): Unit =
    manager.prepareTestInstance(this)

  override protected def beforeEach(): Unit =
    initTest()

  override protected def afterAll(): Unit =
    manager.afterTestClass()

  def persist[E](e: E): E =
    transactionTemplate.execute(_ => entityManager.persist(e))

  private def initTest(): Unit = {
    val meters = (1 to 10).toList
      .map(x => Meter("meter_" + x, Option("label_of_" + x)))
      .map(persist)
    meters.foreach(meter => {
      (1 to 4).toList.map(x => Alert(meter, "alert", x)).map(persist)
      (1 to 6).toList.map(x => Notification(meter, "notification1", x)).map(persist)
      (1 to 2).toList.map(x => Notification(meter, "notification2", x)).map(persist)
    })
  }
}
