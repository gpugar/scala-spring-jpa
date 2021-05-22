package com.code.overflow.demo.spring.repository.jpa

import com.code.overflow.demo.spring.BaseScalaJpaTest
import com.code.overflow.demo.spring.model.Notification
import com.code.overflow.demo.spring.repository.{MeterRepository, NotificationRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest


class NotificationRepositoryImplSpec extends BaseScalaJpaTest {
  @Autowired
  var notificationRepository: NotificationRepository = _

  @Autowired
  var meterRepository: MeterRepository = _

  describe("NotificationRepository") {
    describe("add") {
      it("should save notification") {
        val meterName = "meter_1"
        val meter = meterRepository.get(meterName).getOrElse(fail())
        val notificationLabel = "notification-label"
        val notificationValue = 10
        val notification = notificationRepository.add(Notification(meter, notificationLabel, notificationValue))
        notification.id should not be null
        notification.label should equal(notificationLabel)
        notification.meterValue should equal(notificationValue)
      }
    }

    describe("list") {
      it("should find page one of notifications") {
        val page = notificationRepository.list(PageRequest.of(0, 9))
        page.getTotalElements should equal(80)
        page.getNumberOfElements should equal(9)
        page.getTotalPages should equal(9)
      }
      it("should find last page of notifications") {
        val page = notificationRepository.list(PageRequest.of(8, 9))
        page.getTotalElements should equal(80)
        page.getNumberOfElements should equal(8)
        page.getTotalPages should equal(9)
      }
    }

    describe("listForMeterAndLabel") {
      it("should find page of notifications filtered by meter and label") {
        val page = notificationRepository.listForMeterAndLabel("meter_1", "notification", PageRequest.of(0, 9))
        page.getTotalElements should equal(8)
        page.getNumberOfElements should equal(8)
        page.getTotalPages should equal(1)
      }
    }
  }
}
