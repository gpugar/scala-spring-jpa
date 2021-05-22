package com.code.overflow.demo.spring

import org.scalatest.{BeforeAndAfterEachTestData, Suite, TestData}
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.{PlatformTransactionManager, TransactionStatus}

trait RollbackEachTransactionalTest extends BeforeAndAfterEachTestData { this: Suite =>
  var transactionManager: PlatformTransactionManager
  var rollbackContext: TransactionStatus = _

  override protected def beforeEach(testData: TestData): Unit = {
    super.beforeEach(testData)
    val annotationParser = new SpringTransactionAnnotationParser
    val transactionDefinition = new DefaultTransactionDefinition()
    transactionDefinition.setName(testData.name)
    Option(annotationParser.parseTransactionAnnotation(this.getClass)).foreach(
      annotationAttribute => {
        transactionDefinition.setIsolationLevel(annotationAttribute.getIsolationLevel)
        transactionDefinition.setPropagationBehavior(annotationAttribute.getPropagationBehavior)
        transactionDefinition.setReadOnly(annotationAttribute.isReadOnly)
      }
    )
    rollbackContext = transactionManager.getTransaction(transactionDefinition)
  }

  override protected def afterEach(testData: TestData): Unit = {
    super.afterEach(testData)
    transactionManager.rollback(rollbackContext)
  }
}
